package org.morkato.bmt.loader;

import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.hooks.NameCommandPointer;
import org.morkato.bmt.management.CommandManager;
import org.morkato.bmt.management.RegisterManagement;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class SimpleComponentLoaderFactory implements LoaderRegistrationFactory {
  private final Map<Class<?>, Consumer<Object>> factories = new HashMap<>();
  private final CommandManager commands;
  private final RegisterManagement<CommandException<?>> exceptions;
  private final RegisterManagement<ObjectParser<?>> arguments;
  private final Set<ObjectParser<?>> flipperObjectParser = new HashSet<>();
  private final Set<CommandException<?>> flipperCommandException = new HashSet<>();
  private final Set<Command<?>> flipperCommand = new HashSet<>();
  private final Map<String, Class<? extends Command<?>>> commandsNames = new HashMap<>();
  public SimpleComponentLoaderFactory(
    CommandManager commands,
    RegisterManagement<CommandException<?>> exceptions,
    RegisterManagement<ObjectParser<?>> arguments
  ) {
    this.commands = commands;
    this.exceptions = exceptions;
    this.arguments = arguments;
    factories.put(ObjectParser.class, this::registerObjectParser);
    factories.put(CommandException.class, this::registerCommandException);
    factories.put(Command.class, this::registerCommand);
    factories.put(NameCommandPointer.class, this::registerCommandName);
  }

  public void registerObjectParser(Object o) {
    this.flipperObjectParser.add((ObjectParser<?>)o);
  }
  public void registerCommand(Object o) {
    flipperCommand.add((Command<?>)o);
  }
  public void registerCommandException(Object o) {
    flipperCommandException.add((CommandException<?>)o);
  }
  public void registerCommandName(Object o) {
    NameCommandPointer pointer = (NameCommandPointer)o;
    commandsNames.put(pointer.getName(), pointer.getCommand());
  }

  @Override
  public Consumer<Object> create(Class<?> clazz) {
    return factories.get(clazz);
  }

  @Override
  public void flush() {
    /* Registro de todos os bounders, comandos e parsers. Chamado quando a aplicação está consolidada. */
    RegisterManagement.registerAll(arguments, flipperObjectParser);
    RegisterManagement.registerAll(exceptions, flipperCommandException);
    RegisterManagement.registerAll(commands, flipperCommand);
    for (Map.Entry<String, Class<? extends Command<?>>> entry : commandsNames.entrySet()) {
      String name = entry.getKey();
      Class<? extends Command<?>> command = entry.getValue();
      commands.setCommandName(command, name);
    }
    flipperObjectParser.clear();
    flipperCommandException.clear();
    flipperCommand.clear();
    commandsNames.clear();
  }
}
