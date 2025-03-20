package org.morkato.bmt.bmt.registration.impl;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.bmt.registration.TextCommandExceptionRegistration;
import org.morkato.bmt.bmt.components.CommandException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TextCommandExceptionRegistrationImpl implements TextCommandExceptionRegistration {
  private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandExceptionRegistration.class);
  private final Map<Class<? extends Throwable>, CommandException<?>> exceptions = new HashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Throwable> CommandException<T> get(Class<T> clazz) {
    CommandException<?> handler = exceptions.get(clazz);
    if (handler != null)
      return (CommandException<T>) handler;
    Class<?> current = clazz.getSuperclass();
    while (current != null && Throwable.class.isAssignableFrom(current)) {
      handler = exceptions.get(current);
      if (handler != null) {
        return (CommandException<T>) handler;
      }
      current = current.getSuperclass();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Class<? extends Throwable> registerWithErrors(CommandException<?> bounder) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(bounder.getClass(), CommandException.class);
    Class<? extends Throwable> exception = (Class<? extends Throwable>)typeArguments.values().iterator().next();
    this.exceptions.put(exception, bounder);
    return exception;
  }

  @Override
  public void register(CommandException<?> bounder) {
    Class<? extends Throwable> exception = this.registerWithErrors(bounder);
    LOGGER.info("Error bounder: {} from: {} is registered.", exception.getName(), bounder.getClass().getName());
  }

  @Override
  public void clear() {
    exceptions.clear();
  }
}
