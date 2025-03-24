package org.morkato.bmt.registration.hooks;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.registration.RegisterManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class TextCommandExceptionRegistration
  implements RegisterManagement<CommandException<?>>{
  private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandExceptionRegistration.class);
  private final Set<CommandException<?>> items = new HashSet<>();

  @SuppressWarnings("unchecked")
  public Class<? extends Throwable> registerWithErrors(CommandException<?> bounder) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(bounder.getClass(), CommandException.class);
    Class<? extends Throwable> exception = (Class<? extends Throwable>)typeArguments.values().iterator().next();
    this.items.add(bounder);
    return exception;
  }

  @Override
  public void register(CommandException<?> bounder) {
    Class<? extends Throwable> exception = this.registerWithErrors(bounder);
    LOGGER.info("Error bounder: {} from: {} has been registered.", exception.getName(), bounder.getClass().getName());
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public int size() {
    return items.size();
  }

  @Override
  @Nonnull
  public Iterator<CommandException<?>> iterator() {
    return items.iterator();
  }
}
