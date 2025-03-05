package org.morkato.bmt.management;

import org.morkato.bmt.components.CommandException;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class CommandExceptionManager implements RegisterManagement<CommandException<?>>{
  private static final Logger logger = LoggerFactory.getLogger(CommandExceptionManager.class);
  private final Map<Class<? extends Throwable>, CommandException<?>> exceptions = new HashMap<>();
  @SuppressWarnings("unchecked")
  @Override
  public void register(CommandException<?> bounder) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(bounder.getClass(), CommandException.class);
    Class<? extends Throwable> exception = (Class<? extends Throwable>) typeArguments.values().iterator().next();
    this.exceptions.put(exception, bounder);
    logger.info("Error bounder: {} from: {} is registered.", exception.getName(), bounder.getClass().getName());
  }
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
}
