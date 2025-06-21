package org.morkato.bmt;

import org.morkato.bmt.startup.management.StartupCommandsManagement;
import org.morkato.bmt.startup.management.StartupExceptionHandlerManagement;
import org.morkato.bmt.startup.management.StartupParsersManagement;
import org.morkato.bmt.components.CommandExceptionHandler;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.startup.AppCommandTree;
import org.morkato.bmt.startup.payload.CommandPayload;
import org.morkato.bmt.internal.context.BotContextInternal;
import org.morkato.bmt.startup.BotRegistrationFactory;
import org.morkato.bmt.startup.payload.SlashCommandPayload;
import org.morkato.boot.ExtensionContextFactory;
import org.morkato.bmt.context.BotContext;
import org.morkato.boot.Extension;

import java.util.Objects;

public class BotContextFactory implements ExtensionContextFactory<BotContext> {
  private final BotRegistrationFactory factory;

  public BotContextFactory(BotRegistrationFactory factory) {
    this.factory = Objects.requireNonNull(factory);
  }

  @Override
  public BotContext make(Extension<BotContext> extension) {
    return new BotContextInternal(extension);
  }

  @Override
  public void commit(Extension<BotContext> extension, BotContext context) {
    final AppCommandTree tree = context.getAppCommandsTree();
    final StartupParsersManagement parsers = factory.getStartupParsersManagement();
    final StartupExceptionHandlerManagement exceptions = factory.getStartupExceptionHandlerManagement();
    final StartupCommandsManagement commands = factory.getStartupCommandsManagement();
    for (ObjectParser<?> parser : tree.getPendingArguments())
      parsers.registerObjectParser(ObjectParser.getArgument(parser.getClass()), parser);
    for (SlashMapper<?> mapper : tree.getSlashCommandMappers())
      parsers.registerSlashMapper(SlashMapper.getArgument(mapper.getClass()), mapper);
    for (CommandExceptionHandler<?> bounder : tree.getPendingCommandExceptions())
      exceptions.registerCommandExceptionHandler(CommandExceptionHandler.getArgument(bounder), bounder);
    for (CommandPayload<?> payload : tree.getPendingCommands())
      commands.registerTextCommand(payload);
    for (SlashCommandPayload<?> slashcommand : tree.getPendingSlashCommands())
      commands.registerSlashCommand(slashcommand);
  }
}
