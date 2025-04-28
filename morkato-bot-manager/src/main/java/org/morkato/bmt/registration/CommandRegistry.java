package org.morkato.bmt.registration;

import org.morkato.bmt.registration.attributes.CommandAttributes;
import org.morkato.bmt.impl.TextCommandContextImpl;
import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.components.ObjectParser;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.components.Command;
import org.morkato.utility.StringView;
import org.morkato.bmt.NoArgs;

public class CommandRegistry<T> {
  private final String[] aliases;
  private final String description;
  private final Command<T> command;
  private final ObjectParserRegistry<T> parser;
  private final Class<T> args;

  @SuppressWarnings("unchecked")
  public CommandRegistry(
    CommandAttributes attributes,
    Command<T> command,
    ObjectParserRegistry<T> parser
  ) {
    this.args = (Class<T>)Command.getArgument(command.getClass());
    this.command = command;
    this.parser = parser;
    this.aliases = attributes.getAliases();
    this.description = attributes.getDescription();
  }

  @SuppressWarnings("unchecked")
  public Class<? extends Command<T>> getCommandClass() {
    return (Class<? extends Command<T>>)this.command.getClass();
  }

  public String getCommandClassName() {
    return this.getCommandClass().getName();
  }

  public TextCommandContextImpl<T> spawnContext(Message message) {
    return new TextCommandContextImpl<>(command, message, null);
  }

  public void prepareContext(TextCommandContextImpl<T> context, StringView view) throws Throwable {
    view.skipWhitespace();
    if (args != NoArgs.class && !view.eof()) {
      T argument = parser.parse(context, view.rest());
      context.setArgs(argument);
    }
  }

  public void invoke(TextCommandContext<T> context) throws Throwable {
    command.invoke(context);
  }

  public String[] getAliases() {
    return aliases;
  }

  public String getDescription() {
    return description;
  }
}