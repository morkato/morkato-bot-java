package org.morkato.bmt.registration.hooks;

import org.morkato.bmt.registration.registries.TextCommandExceptionRegistry;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.components.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class TextCommandExceptionRegistration extends ObjectRegistration<TextCommandExceptionRegistry, CommandException<?>> {
  private final Logger LOGGER = LoggerFactory.getLogger(TextCommandExceptionRegistration.class);

  @SuppressWarnings("unchecked")
  public Class<? extends Throwable> registerWithErrors(CommandException<?> bounder) {
    Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(bounder.getClass(), CommandException.class);
    Class<? extends Throwable> exception = (Class<? extends Throwable>)typeArguments.values().iterator().next();
    TextCommandExceptionRegistry registry = new TextCommandExceptionRegistry(bounder, exception);
    this.add(exception, registry);
    return exception;
  }

  @Override
  public void register(CommandException<?> bounder) {
    Class<? extends Throwable> exception = this.registerWithErrors(bounder);
    LOGGER.info("Error bounder: {} from: {} has been registered.", exception.getName(), bounder.getClass().getName());
  }
}
