package org.morkato.bmt.registration;

import org.morkato.bmt.CommandRegistry;
import org.morkato.bmt.SubCommandPayload;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.registration.impl.TextCommandRegistrationImpl;

public interface TextCommandRegistration extends RegisterManagement<Command<?>> {
  static TextCommandRegistration createDefault(TextCommandExceptionRegistration exceptions, ArgumentRegistration arguments) {
    return new TextCommandRegistrationImpl(exceptions, arguments);
  }
  void registerSubCommand(SubCommandPayload payload);
  CommandRegistry<?> getRegistry(Class<? extends Command<?>> registry);
}
