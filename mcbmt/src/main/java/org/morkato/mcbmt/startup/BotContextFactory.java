package org.morkato.mcbmt.startup;

import org.morkato.mcbmt.components.CommandHandler;
import org.morkato.mcbmt.startup.builder.AppBuilder;
import org.morkato.mcbmt.startup.management.StartupActionsManagement;
import org.morkato.mcbmt.startup.management.StartupCommandsManagement;
import org.morkato.mcbmt.startup.management.StartupExceptionHandlerManagement;
import org.morkato.mcbmt.startup.management.StartupParsersManagement;
import org.morkato.mcbmt.components.CommandExceptionHandler;
import org.morkato.mcbmt.components.ObjectParser;
import org.morkato.mcbmt.components.SlashMapper;
import org.morkato.mcbmt.startup.payload.ActionPayload;
import org.morkato.mcbmt.startup.payload.CommandPayload;
import org.morkato.mcbmt.BotContextImpl;
import org.morkato.mcbmt.startup.payload.SlashCommandPayload;
import org.morkato.boot.ExtensionContextFactory;
import org.morkato.mcbmt.BotContext;
import org.morkato.boot.Extension;
import java.util.Objects;

public class BotContextFactory implements ExtensionContextFactory<BotContext> {
  private final BotRegistrationFactory factory;

  public BotContextFactory(BotRegistrationFactory factory) {
    this.factory = Objects.requireNonNull(factory);
  }

  @Override
  public BotContext make(Extension<BotContext> extension) {
    return new BotContextImpl(extension);
  }

  @Override
  public void commit(Extension<BotContext> extension, BotContext context) {
    final AppBuilder tree = context.getAppCommandsTree();
    final StartupParsersManagement parsers = factory.getStartupParsersManagement();
    final StartupExceptionHandlerManagement exceptions = factory.getStartupExceptionHandlerManagement();
    final StartupActionsManagement actions = factory.getStartupActionsManagement();
    final StartupCommandsManagement commands = factory.getStartupCommandsManagement();
    for (ObjectParser<?> parser : tree.getPendingArguments())
      parsers.registerObjectParser(ObjectParser.getArgument(parser.getClass()), parser);
    for (SlashMapper<?> mapper : tree.getSlashCommandMappers())
      parsers.registerSlashMapper(SlashMapper.getArgument(mapper.getClass()), mapper);
    for (CommandExceptionHandler<?> bounder : tree.getPendingCommandExceptions())
      exceptions.registerCommandExceptionHandler(CommandExceptionHandler.getArgument(bounder), bounder);
    for (ActionPayload<?> actionpayload : tree.getPendingActions())
      actions.registerAction(actionpayload);
    for (CommandHandler<?> handler : tree.getPendingCommandHandlers())
      commands.registerCommandHandler(handler);
    for (CommandPayload<?> payload : tree.getPendingCommands())
      commands.registerTextCommand(payload);
    for (SlashCommandPayload<?> slashcommand : tree.getPendingSlashCommands())
      commands.registerSlashCommand(slashcommand);
  }
}
