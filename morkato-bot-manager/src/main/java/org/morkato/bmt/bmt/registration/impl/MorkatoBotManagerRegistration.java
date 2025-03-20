package org.morkato.bmt.bmt.registration.impl;

import org.morkato.bmt.bmt.components.CommandException;
import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.bmt.components.Command;
import org.morkato.bmt.bmt.registration.*;

import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class MorkatoBotManagerRegistration implements RegistrationFactory{
  private final Map<Class<?>, Consumer<Object>> factories = new HashMap<>();
  private final RecordsRegistrationProxy<CommandException<?>,TextCommandExceptionRegistration> textCommandExceptions;
  private final RecordsRegistrationProxy<ObjectParser<?>,ArgumentRegistration> arguments;
  private final RecordsRegistrationProxy<Command<?>,TextCommandRegistration> textCommands;

  public MorkatoBotManagerRegistration() {
    TextCommandExceptionRegistration textCommandExceptions = TextCommandExceptionRegistration.createDefault();
    ArgumentRegistration arguments = ArgumentRegistration.createDefault();
    TextCommandRegistration commands = TextCommandRegistration.createDefault(textCommandExceptions, arguments);
    this.textCommandExceptions = new RecordsRegistrationProxy<>(textCommandExceptions);
    this.arguments = new RecordsRegistrationProxy<>(arguments);
    this.textCommands = new RecordsRegistrationProxy<>(commands);
    factories.put(CommandException.class, o -> this.textCommandExceptions.register((CommandException<?>)o));
    factories.put(ObjectParser.class, o -> this.arguments.register((ObjectParser<?>)o));
    factories.put(Command.class, o -> this.textCommands.register((Command<?>)o));
  }

  public TextCommandExceptionRegistration getTextCommandExceptionRegistration() {
    return this.textCommandExceptions.getOriginal();
  }

  public ArgumentRegistration getArgumentRegistration() {
    return this.arguments.getOriginal();
  }

  public TextCommandRegistration getTextCommandRegistration() {
    return this.textCommands.getOriginal();
  }

  @Override
  public Consumer<Object> create(Class<?> clazz) {
    return factories.get(clazz);
  }

  @Override
  public void flush() {
    /* Registro de todos os bounders, comandos e parsers. Chamado quando a aplicação está consolidada. */
    arguments.flush();
    textCommandExceptions.flush();
    textCommands.flush();
  }

  public void clear() {
    arguments.clear();
    textCommandExceptions.clear();
    textCommands.clear();
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
