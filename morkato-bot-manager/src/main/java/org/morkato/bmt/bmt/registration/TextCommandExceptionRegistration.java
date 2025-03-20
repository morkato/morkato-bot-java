package org.morkato.bmt.bmt.registration;

import org.morkato.bmt.bmt.components.CommandException;
import org.morkato.bmt.bmt.management.RegisterManagement;
import org.morkato.bmt.bmt.registration.impl.TextCommandExceptionRegistrationImpl;

public interface TextCommandExceptionRegistration extends RegisterManagement<CommandException<?>> {
  static TextCommandExceptionRegistration createDefault() {
    return new TextCommandExceptionRegistrationImpl();
  }
  <T extends Throwable> CommandException<T> get(Class<T> clazz);
}
