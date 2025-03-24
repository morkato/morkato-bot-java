package org.morkato.bmt.registration;

import org.morkato.bmt.ApplicationRegistries;
import org.morkato.bmt.DependenceInjection;
import java.util.function.Consumer;

public interface RegistrationFactory<T> {
  static RegistrationFactory<ApplicationRegistries> createDefault() {
    return new MorkatoBotManagerRegistration();
  }

  /* Cria o contexto sobre carga de registro de comandos, transformadores, extensões e eventos. Lançará um erro caso o contexto seja indisponível. */
  Consumer<Object> create(Class<?> clazz);
  void prepare(DependenceInjection injector);
  T flush();
  void shutdown();
}
