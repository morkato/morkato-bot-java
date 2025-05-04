package org.morkato.bmt.generated.registries;

import org.morkato.bmt.internal.context.TextCommandContext;
import org.morkato.bmt.registration.attributes.CommandAttributes;
import org.morkato.bmt.context.CommandContext;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.utility.StringView;
import org.morkato.bmt.NoArgs;

public class CommandRegistry<T> {
  private final String name;
  private final String[] aliases;
  private final String description;
  private final CommandHandler<T> command;
  private final ObjectParserRegistry<T> parser;
  private final Class<T> args;

  @SuppressWarnings("unchecked")
  public CommandRegistry(
    CommandAttributes attributes,
    CommandHandler<T> command,
    ObjectParserRegistry<T> parser
  ) {
    this.args = (Class<T>)CommandHandler.getArgument(command.getClass());
    this.command = command;
    this.parser = parser;
    this.aliases = attributes.getAliases();
    this.name = attributes.getName();
    this.description = attributes.getDescription();
  }

  @SuppressWarnings("unchecked")
  public Class<? extends CommandHandler<T>> getCommandClass() {
    return (Class<? extends CommandHandler<T>>)this.command.getClass();
  }

  public String getCommandClassName() {
    return this.getCommandClass().getName();
  }

  public TextCommandContext<T> spawnContext(Message message) {
    return new TextCommandContext<>(command, message, null);
  }

  public void prepareContext(TextCommandContext<T> context, StringView view) throws Throwable {
    view.skipWhitespace();
    if (args != NoArgs.class && !view.eof()) {
      T argument = parser.parse(context, view.rest());
      context.setArgs(argument);
    }
  }

  public void invoke(CommandContext<T> context) throws Throwable {
    command.invoke(context);
  }

  public String getName() {
    return name;
  }

  public String[] getAliases() {
    return aliases;
  }

  public String getDescription() {
    return description;
  }
}