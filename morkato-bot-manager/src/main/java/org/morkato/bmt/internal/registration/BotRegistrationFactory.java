package org.morkato.bmt.internal.registration;

import org.morkato.bmt.ApplicationStaticRegistries;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.registration.CommandExceptionRegistry;
import org.morkato.bmt.registration.CommandRegistry;
import org.morkato.bmt.registration.ObjectParserRegistry;
import org.morkato.bmt.registration.RegistrationInterceptor;
import org.morkato.bmt.registration.attributes.CommandAttributes;
import org.morkato.bmt.registration.management.CommandExceptionManagement;
import org.morkato.bmt.registration.management.CommandRegistryManagement;
import org.morkato.bmt.registration.management.ObjectParserRegistryManagement;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import java.util.Objects;

public class BotRegistrationFactory implements RegistrationFactory<ApplicationStaticRegistries> {
  private final DependenceInjection injector;
  private final RegistrationInterceptor<ObjectParser<?>, ObjectParserRegistry<?>> parsers;
  private final RegistrationInterceptor<CommandPayload<?>, CommandRegistry<?>> commands;
  private final RegistrationInterceptor<CommandException<?>, CommandExceptionRegistry<?>> exceptions;

  public BotRegistrationFactory(DependenceInjection injector) {
    this.injector = Objects.requireNonNull(injector);
    ObjectParserRegistryManagement parsers = ObjectParserRegistryManagement.getDefault();
    this.parsers = new RegistrationInterceptor<>(parsers, injector);
    this.commands = new RegistrationInterceptor<>(new CommandRegistryManagement(parsers), injector);
    this.exceptions = new RegistrationInterceptor<>(new CommandExceptionManagement(), injector);
  }

  public void registerObjectParser(ObjectParser<?> parser) {
    parsers.register(parser);
  }

  public <T> void registerCommand(Command<T> command, CommandAttributes attributes) {
    commands.register(new CommandPayload<>(command, attributes));
  }

  public void registerCommandException(CommandException<?> bounder) {
    exceptions.register(bounder);
  }

  @Override
  public ApplicationStaticRegistries flush() {
    return new ApplicationStaticRegistries(
      parsers.compute(),
      exceptions.compute(),
      commands.compute()
    );
  }
}
