package org.morkato.bmt.registration;

import org.morkato.bmt.registration.impl.MorkatoBotManagerRegistration;
import org.morkato.utility.ClassInjectorMap;
import java.util.function.Consumer;

public interface RegistrationFactory {
  static MorkatoBotManagerRegistration createDefault() {
    return new MorkatoBotManagerRegistration();
  }

  /* Cria o contexto sobre carga de registro de comandos, transformadores, extensões e eventos. Lançará um erro caso o contexto seja indisponível. */
  Consumer<Object> create(Class<?> clazz);
  void prepare(ClassInjectorMap injector);
  void flush();
  void shutdown();
}
