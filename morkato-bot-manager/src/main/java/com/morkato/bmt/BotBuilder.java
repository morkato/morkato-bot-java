package com.morkato.bmt;

import com.morkato.bmt.commands.CommandManager;
import com.morkato.bmt.errors.*;
import com.morkato.bmt.annotation.AutoInject;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class BotBuilder {
  private final Logger logger = LoggerFactory.getLogger(BotBuilder.class);
  private final String token;

  public BotBuilder(
    @NotNull String token
  ) {
    this.token = token;
  }
  private void inject(
    @NotNull Object extension,
    @NotNull Field field,
    @NotNull Map<Class<?>, Object> injected
  ) throws FieldInjectionError, FieldNotIsPublic, ObjectNotFound {
    if (!Modifier.isPublic(field.getModifiers()))
      throw new FieldNotIsPublic(field);
    Object object = injected.get(field.getType());
    if (object == null)
      throw new ObjectNotFound(field);
    try {
      field.set(extension, object);
    } catch (Exception exc) {
      throw new FieldInjectionError(field);
    }
  }
  private void inject(
    @NotNull Object extension,
    @NotNull Map<Class<?>, Object> injected
  )
    throws FieldInjectionError, FieldNotIsPublic, ObjectNotFound {
    Class<?> cls = extension.getClass();
    Field[] fields = cls.getDeclaredFields();
    for (Field field : fields) {
      if (!field.isAnnotationPresent(AutoInject.class))
        continue;
      this.inject(extension, field, injected);
    }
  }
  @SuppressWarnings("unchecked")
  private <T> Map<Class<? extends T>, T> injectAll(
    @NotNull Map<Class<? extends T>, T> objects,
    @NotNull Map<Class<?>, Object> injected
  ) {
    Map<Class<? extends T>, T> injectedObjects = new HashMap<>();
    for (T object : objects.values()) {
      try {
        this.inject(object, injected);
        injectedObjects.put((Class<? extends T>)object.getClass(), object);
      } catch (InjectionException exc) {
        logger.warn(exc.getMessage());
      }
    }
    return injectedObjects;
  }
  public JDA build(
    @Nonnull ExtensionManager manager,
    @NotNull CommandManager commands,
    @Nonnull Map<Class<?>, Object> injected
  ) throws Exception {
    JDABuilder builder = JDABuilder.createDefault(token);
    BotListener baseListener = new BotListener(manager, commands, injected);
    builder.addEventListeners(baseListener);
    return builder
      .enableIntents(GatewayIntent.MESSAGE_CONTENT)
      .build()
      .awaitReady();
  }
}
