package org.morkato.bmt.registration;

import org.morkato.bmt.registration.impl.NamePointerRegistrationImpl;
import org.morkato.bmt.hooks.NamePointer;
import org.morkato.bmt.CommandRegistry;

public interface NamePointerRegistration extends RegisterManagement<NamePointer> {
  static NamePointerRegistration createDefault(TextCommandRegistration registration) {
    return new NamePointerRegistrationImpl(registration);
  }
  CommandRegistry<?> getCommandRegistry(String pointer);
}
