package org.morkato.bmt.bmt.registration;

import org.morkato.bmt.bmt.commands.CommandRegistry;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.management.RegisterManagement;
import org.morkato.bmt.bmt.registration.impl.TextCommandRegistrationImpl;

public interface TextCommandRegistration extends RegisterManagement<Command<?>> {
  static TextCommandRegistration createDefault(TextCommandExceptionRegistration exceptions, ArgumentRegistration arguments) {
    return new TextCommandRegistrationImpl(exceptions, arguments);
  }
  CommandRegistry<?> getRegistry(Class<? extends Command<?>> registry);
}
