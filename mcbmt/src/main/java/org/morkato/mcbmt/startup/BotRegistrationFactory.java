package org.morkato.mcbmt.startup;

import org.morkato.mcbmt.BotCore;
import org.morkato.mcbmt.generated.ActionsStaticRegistries;
import org.morkato.mcbmt.generated.CommandsStaticRegistries;
import org.morkato.mcbmt.generated.ExceptionsHandleStaticRegistries;
import org.morkato.mcbmt.generated.ParsersStaticRegistries;
import org.morkato.mcbmt.startup.management.StartupActionsManagement;
import org.morkato.mcbmt.startup.management.StartupExceptionHandlerManagement;
import org.morkato.mcbmt.startup.management.StartupCommandsManagement;
import org.morkato.mcbmt.startup.management.StartupParsersManagement;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import java.util.Objects;

public class BotRegistrationFactory implements RegistrationFactory<BotCore> {
  private final DependenceInjection injector;
  private final StartupParsersManagement parsers;
  private final StartupExceptionHandlerManagement exceptions;
  private final StartupActionsManagement actions;
  private final StartupCommandsManagement commands;

  public BotRegistrationFactory(DependenceInjection injector) {
    this.injector = Objects.requireNonNull(injector);
    this.parsers = new StartupParsersManagement(injector);
    this.exceptions = new StartupExceptionHandlerManagement(injector);
    this.actions = new StartupActionsManagement(injector);
    this.commands = new StartupCommandsManagement(injector);
  }

  public StartupParsersManagement getStartupParsersManagement() {
    return parsers;
  }

  public StartupExceptionHandlerManagement getStartupExceptionHandlerManagement() {
    return exceptions;
  }

  public StartupActionsManagement getStartupActionsManagement(){
    return actions;
  }

  public StartupCommandsManagement getStartupCommandsManagement() {
    return commands;
  }

  @Override
  public BotCore flush() {
    final ParsersStaticRegistries staticParsers = parsers.flush();
    final ExceptionsHandleStaticRegistries staticExceptions = exceptions.flush();
    final ActionsStaticRegistries staticActions = actions.flush();
    commands.prepare(staticParsers, staticActions);
    final CommandsStaticRegistries staticCommands = commands.flush();
    return new BotCore(staticParsers, staticExceptions, staticActions, staticCommands);
  }

}
