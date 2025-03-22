package org.morkato.bmt.registration.impl;

import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.hooks.NamePointer;
import org.morkato.bmt.registration.*;
import org.morkato.utility.ClassInjectorMap;

import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class MorkatoBotManagerRegistration implements RegistrationFactory {
  private final Map<Class<?>, Consumer<Object>> factories = new HashMap<>();
  private final RecordsRegistrationProxy<CommandException<?>, TextCommandExceptionRegistration> textCommandExceptions;
  private final RecordsRegistrationProxy<ObjectParser<?>, ArgumentRegistration> arguments;
  private final RecordsRegistrationProxy<Command<?>, TextCommandRegistration> textCommands;
  private final RecordsRegistrationProxy<NamePointer, NamePointerRegistration> pointers;
  public MorkatoBotManagerRegistration() {
    TextCommandExceptionRegistration textCommandExceptions = TextCommandExceptionRegistration.createDefault();
    ArgumentRegistration arguments = ArgumentRegistration.createDefault();
    TextCommandRegistration commands = TextCommandRegistration.createDefault(textCommandExceptions, arguments);
    NamePointerRegistration pointers = NamePointerRegistration.createDefault(commands);
    this.textCommandExceptions = new RecordsRegistrationProxy<>(textCommandExceptions);
    this.arguments = new RecordsRegistrationProxy<>(arguments);
    this.textCommands = new RecordsRegistrationProxy<>(commands);
    this.pointers = new RecordsRegistrationProxy<>(pointers);
    factories.put(CommandException.class, o -> this.textCommandExceptions.register((CommandException<?>)o));
    factories.put(ObjectParser.class, o -> this.arguments.register((ObjectParser<?>)o));
    factories.put(Command.class, o -> this.textCommands.register((Command<?>)o));
    factories.put(NamePointer.class, o -> this.pointers.register((NamePointer)o));
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

  public NamePointerRegistration getNamePointerRegistration() {
    return this.pointers.getOriginal();
  }

  @Override
  public Consumer<Object> create(Class<?> clazz) {
    return factories.get(clazz);
  }

  @Override
  public void prepare(ClassInjectorMap injector) {
    arguments.prepare(injector);
    textCommandExceptions.prepare(injector);
    textCommands.prepare(injector);
    pointers.prepare(injector);
  }

  @Override
  public void flush() {
    /* Registro de todos os bounders, comandos e parsers. Chamado quando a aplicação está consolidada. */
    arguments.flush();
    textCommandExceptions.flush();
    textCommands.flush();
    pointers.flush();
  }

  public void clear() {
    arguments.clear();
    textCommandExceptions.clear();
    textCommands.clear();
    pointers.clear();
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
