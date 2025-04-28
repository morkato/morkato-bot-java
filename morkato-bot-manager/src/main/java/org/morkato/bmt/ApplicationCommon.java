package org.morkato.bmt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.internal.registration.BotRegistrationFactory;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.Extension;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class ApplicationCommon extends ApplicationBot {
  private final Class<?> mainClazz;
  private final Properties properties;
  private final String token;

  public ApplicationCommon(Class<?> mainClazz, String token, Properties properties) {
    super();
    this.mainClazz = Objects.requireNonNull(mainClazz);
    this.token = Objects.requireNonNull(token);
    this.properties = Objects.requireNonNull(properties);
  }

  @Override
  protected BotRegistrationFactory createFactory(DependenceInjection injector) {
    return new BotRegistrationFactory(injector);
  }

  @Override
  protected DependenceInjection createDependenceInjection() {
    final DependenceInjection injector = DependenceInjection.createDefault();
    for (String name : properties.stringPropertyNames()) {
      injector.setProperty(name, properties.getProperty(name));
    }
    return injector;
  }

  protected Collection<Extension<BotContext>> createExtensions() {
    return Set.of();
  }

  @Override
  protected Collection<ListenerAdapter> createListeners() {
    return Set.of();
  }

  @Override
  protected Collection<GatewayIntent> createIntents() {
    return Set.of();
  }

  @Override
  protected String getToken() {
    return token;
  }


  @Override
  protected void onReady(JDA jda, ApplicationStaticRegistries registries) {
    LOGGER.info("All components have been initialized successfully.");
    LOGGER.info("Totally components initialized: {} (Including JDA).", registries.totally());
    LOGGER.info("Estou conectado, como: {} (id={})", jda.getSelfUser().getAsTag(), jda.getSelfUser().getId());
  }

  @Override
  protected void close() {
    LOGGER.info("Shutting down application and cleaning up resources.");
    /* Invocado para fechamento: SIGTERM etc... Pode fechar processador de comandos (Com threads alugadas, eventloops, etc...) */
  }

  @Override
  public void run() throws Throwable {
    LOGGER.info("Launching bot: [{}] PID: {}", mainClazz.getSimpleName(), ManagementFactory.getRuntimeMXBean().getPid());
    LOGGER.info("Creating a context to run discord bot ({} -- {}).", ApplicationCommon.class.getPackageName(), mainClazz.getPackageName());
    super.run();
  }
}
