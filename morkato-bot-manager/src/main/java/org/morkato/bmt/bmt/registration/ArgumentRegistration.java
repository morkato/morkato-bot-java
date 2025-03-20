package org.morkato.bmt.bmt.registration;

import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.bmt.management.RegisterManagement;
import org.morkato.bmt.bmt.registration.impl.ArgumentRegistrationImpl;

public interface ArgumentRegistration extends RegisterManagement<ObjectParser<?>> {
  static ArgumentRegistration createDefault() {
    return ArgumentRegistrationImpl.get();
  }
  <T> ObjectParser<T> getParser(Class<T> clazz);
}
