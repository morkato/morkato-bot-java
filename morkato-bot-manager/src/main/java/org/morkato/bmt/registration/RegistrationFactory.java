package org.morkato.bmt.registration;

import org.morkato.bmt.management.CommandExceptionManager;
import org.morkato.bmt.management.ArgumentManager;
import org.morkato.bmt.management.CommandManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.function.Consumer;

public interface RegistrationFactory {
  static RegistrationFactory createDefault() {
    CommandExceptionManager exceptions = new CommandExceptionManager();
    ArgumentManager arguments = ArgumentManager.get();
    CommandManager commands = new CommandManager(exceptions, arguments);
    return new SimpleLoaderRegistrationFactory(commands, exceptions, arguments);
  }

  /* Cria o contexto sobre carga de registro de comandos, transformadores, extensões e eventos. Lançará um erro caso o contexto seja indisponível. */
  Consumer<Object> create(Class<?> clazz);
  ListenerAdapter createListener();
  void flush();
  void shutdown();
}
