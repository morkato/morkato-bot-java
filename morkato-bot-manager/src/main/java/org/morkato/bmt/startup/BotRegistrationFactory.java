package org.morkato.bmt.startup;

import org.morkato.bmt.startup.management.StartupExceptionHandlerManagement;
import org.morkato.bmt.startup.management.StartupCommandsManagement;
import org.morkato.bmt.startup.management.StartupParsersManagement;
import org.morkato.bmt.generated.ExceptionsHandleStaticRegistries;
import org.morkato.bmt.generated.ApplicationStaticRegistries;
import org.morkato.bmt.generated.CommandsStaticRegistries;
import org.morkato.bmt.generated.ParsersStaticRegistries;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import java.util.Objects;

public class BotRegistrationFactory implements RegistrationFactory<ApplicationStaticRegistries> {
  private final DependenceInjection injector;
  private final StartupParsersManagement parsers;
  private final StartupExceptionHandlerManagement exceptions;
  private final StartupCommandsManagement commands;

  public BotRegistrationFactory(DependenceInjection injector) {
    this.injector = Objects.requireNonNull(injector);
    this.parsers = new StartupParsersManagement(injector);
    this.exceptions = new StartupExceptionHandlerManagement(injector);
    this.commands = new StartupCommandsManagement(injector);
  }

  public StartupParsersManagement getStartupParsersManagement() {
    return parsers;
  }

  public StartupExceptionHandlerManagement getStartupExceptionHandlerManagement() {
    return exceptions;
  }

  public StartupCommandsManagement getStartupCommandsManagement() {
    return commands;
  }

  @Override
  public ApplicationStaticRegistries flush() {
    final ParsersStaticRegistries staticParsers = parsers.flush();
    final ExceptionsHandleStaticRegistries staticExceptions = exceptions.flush();
    commands.prepare(staticParsers);
    final CommandsStaticRegistries staticCommands = commands.flush();
    return new ApplicationStaticRegistries(staticParsers, staticExceptions, staticCommands);
  }
}
