package org.morkato.bmt.loader;

import java.util.function.Consumer;

public interface LoaderRegistrationFactory {
  /* Cria o contexto sobre carga de registro de comandos, transformadores, extensões e eventos. Lançará um erro caso o contexto seja indisponível. */
  Consumer<Object> create(Class<?> clazz);
  void flip();
}
