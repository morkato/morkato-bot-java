package org.morkato.bmt.registration.registries;

import org.morkato.bmt.NoArgs;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.impl.TextCommandContextImpl;
import org.morkato.utility.StringView;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandRegistry<T> implements Registry<Command<?>> {
  private final Map<String, CommandRegistry<?>> subcommands = new HashMap<>();
  private final Command<T> command;
  private final ObjectParser<T> parser;
  private final Class<T> args;

  public CommandRegistry(
    Command<T> command,
    ObjectParser<T> parser,
    Class<T> argumentClazz
  ) {
    this.command = command;
    this.parser = parser;
    this.args = argumentClazz;
  }

  @Override
  public Command<?> getRegistry() {
    return command;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public Class<? extends Command<T>> getCommandClass() {
    return (Class<? extends Command<T>>)this.command.getClass();
  }
  @Nonnull
  public String getCommandClassName() {
    return this.getCommandClass().getName();
  }

  public boolean hasSubCommands() {
    return !this.subcommands.isEmpty();
  }

  public Command<T> getCommand() {
    return command;
  }

  public CommandRegistry<?> getSubCommand(String pointer) {
    return this.subcommands.get(pointer);
  }

  public void registerSubCommand(String pointer, CommandRegistry<?> children) {
    this.subcommands.put(pointer, children);
  }

  public TextCommandContextImpl<T> spawnContext(Message message) {
    return new TextCommandContextImpl<>(command, message, null);
  }

  public void prepareContext(TextCommandContextImpl<T> context, StringView view) throws Throwable {
    view.skipWhitespace();
    if (args != NoArgs.class && !view.eof()) {
      view.skipWhitespace();
      T argument = parser.parse(context, view.rest());
      context.setArgs(argument);
    }
  }

  public void invoke(TextCommandContext<T> context) throws Throwable {
    command.invoke(context);
  }

  public Class<? extends Command<?>> getParentClass() {
    if (Objects.isNull(command.parent()))
      return null;
    return command.parent().parent();
  }

  public String getChildrenName() {
    if (Objects.isNull(command.parent()))
      return null;
    return command.parent().name();
  }
}
