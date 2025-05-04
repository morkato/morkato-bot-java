package org.morkato.bmt.registration.management;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.generated.registries.CommandExceptionRegistry;
import org.morkato.bmt.registration.MapObjectRegisterInfo;
import org.morkato.bmt.registration.RegisterManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.Map;

public class CommandExceptionManagement
  extends MapObjectRegisterInfo<CommandExceptionRegistry<?>>
  implements RegisterManagement<CommandException<?>, CommandExceptionRegistry<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandExceptionManagement.class);

  @SuppressWarnings("unchecked")
  public <T extends Throwable> Class<T> registerWithExceptions(CommandException<T> bounder) {
    Map<TypeVariable<?>,Type> typeArguments = TypeUtils.getTypeArguments(bounder.getClass(), CommandException.class);
    Class<T> exception = (Class<T>)typeArguments.values().iterator().next();
    CommandExceptionRegistry<T> registry = new CommandExceptionRegistry<>(bounder, exception);
    this.add(exception, registry);
    return exception;
  }

  @Override
  public void register(CommandException<?> bounder) {
    Class<? extends Throwable> exception = this.registerWithExceptions(bounder);
    LOGGER.info("Error bounder: {} from: {} has been registered.", exception.getName(), bounder.getClass().getName());
  }
}
