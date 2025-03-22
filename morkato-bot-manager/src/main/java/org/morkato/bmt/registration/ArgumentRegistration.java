package org.morkato.bmt.registration;

import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.registration.impl.ArgumentRegistrationImpl;

public interface ArgumentRegistration extends RegisterManagement<ObjectParser<?>> {
  static ArgumentRegistration createDefault() {
    return ArgumentRegistrationImpl.get();
  }
  <T> ObjectParser<T> getParser(Class<T> clazz);
}
