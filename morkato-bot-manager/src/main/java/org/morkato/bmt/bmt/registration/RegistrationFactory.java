package org.morkato.bmt.bmt.registration;

import org.morkato.bmt.bmt.registration.impl.MorkatoBotManagerRegistration;

import java.util.function.Consumer;

public interface RegistrationFactory {
  static MorkatoBotManagerRegistration createDefault() {
    return new MorkatoBotManagerRegistration();
  }

  /* Cria o contexto sobre carga de registro de comandos, transformadores, extensões e eventos. Lançará um erro caso o contexto seja indisponível. */
  Consumer<Object> create(Class<?> clazz);
  void flush();
  void shutdown();
}
