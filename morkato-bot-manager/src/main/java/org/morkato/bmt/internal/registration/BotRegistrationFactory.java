package org.morkato.bmt.internal.registration;

import org.morkato.bmt.generated.ApplicationStaticRegistries;
import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.SlashMapper;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.generated.registries.*;
import org.morkato.bmt.registration.*;
import org.morkato.bmt.registration.attributes.CommandAttributes;
import org.morkato.bmt.registration.management.*;
import org.morkato.bmt.registration.payload.CommandPayload;
import org.morkato.bmt.registration.payload.SlashCommandPayload;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.registration.RegistrationFactory;
import java.util.Objects;

public class BotRegistrationFactory implements RegistrationFactory<ApplicationStaticRegistries> {
  private final DependenceInjection injector;
  private final RegistrationInterceptor<ObjectParser<?>,ObjectParserRegistry<?>> parsers;
  private final RegistrationInterceptor<CommandPayload<?>,CommandRegistry<?>> commands;
  private final RegistrationInterceptor<CommandException<?>,CommandExceptionRegistry<?>> exceptions;
  private final RegistrationInterceptor<SlashMapper<?>,SlashMapperRegistry<?>> options;
  private final RegistrationInterceptor<SlashCommandPayload<?>,SlashCommandRegistry<?>> slashcommands;

  public BotRegistrationFactory(DependenceInjection injector) {
    this.injector = Objects.requireNonNull(injector);
    ObjectParserRegistryManagement parsers = ObjectParserRegistryManagement.getDefault();
    MapInteractionOptionsRegistryManagement optionsparser = new MapInteractionOptionsRegistryManagement();
    this.parsers = new RegistrationInterceptor<>(parsers, injector);
    this.commands = new RegistrationInterceptor<>(new CommandRegistryManagement(parsers), injector);
    this.exceptions = new RegistrationInterceptor<>(new CommandExceptionManagement(), injector);
    this.options = new RegistrationInterceptor<>(optionsparser, injector);
    this.slashcommands = new RegistrationInterceptor<>(new SlashCommandRegistryManagement(optionsparser), injector);
  }

  public void registerObjectParser(ObjectParser<?> parser) {
    parsers.register(parser);
  }

  public <T> void registerCommand(CommandHandler<T> command,CommandAttributes attributes) {
    commands.register(new CommandPayload<>(command, attributes));
  }

  public void registerSlashCommand(SlashCommandPayload<?> slashcommand) {
    slashcommands.register(slashcommand);
  }

  public void registerSlashMapper(SlashMapper<?> mapper) {
    options.register(mapper);
  }

  public void registerCommandException(CommandException<?> bounder) {
    exceptions.register(bounder);
  }

  @Override
  public ApplicationStaticRegistries flush() {
    options.compute();
    return new ApplicationStaticRegistries(
      parsers.compute(),
      exceptions.compute(),
      commands.compute(),
      slashcommands.compute(),
      options.compute()
    );
  }
}
