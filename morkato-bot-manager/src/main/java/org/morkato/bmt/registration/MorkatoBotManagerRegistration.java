package org.morkato.bmt.registration;

import org.morkato.bmt.registration.registries.ArgumentRegistry;
import org.morkato.bmt.registration.registries.CommandRegistry;
import org.morkato.bmt.DependenceInjection;
import org.morkato.bmt.components.CommandException;
import org.morkato.bmt.components.MessageEmbedBuilder;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.ApplicationRegistries;
import org.morkato.bmt.extensions.Extension;
import org.morkato.bmt.registration.hooks.*;
import org.morkato.bmt.registration.registries.MessageEmbedBuilderRegistry;
import org.morkato.bmt.registration.registries.TextCommandExceptionRegistry;

import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class MorkatoBotManagerRegistration implements RegistrationFactory<ApplicationRegistries> {
  private final Map<Class<?>, Consumer<Object>> factories = new HashMap<>();
  private final ExtensionRegistrationProxy extensions;
  private final RecordsRegistrationProxy<TextCommandExceptionRegistry, CommandException<?>> textCommandExceptions;
  private final RecordsRegistrationProxy<MessageEmbedBuilderRegistry, MessageEmbedBuilder<?>> embeds;
  private final RecordsRegistrationProxy<ArgumentRegistry, ObjectParser<?>> arguments;
  private final RecordsRegistrationProxy<CommandRegistry<?>, Command<?>> textCommands;
  public MorkatoBotManagerRegistration() {
    final ArgumentRegistration arguments = ArgumentRegistration.get();
    this.extensions = new ExtensionRegistrationProxy(new ExtensionRegistration(this));
    this.textCommandExceptions = new RecordsRegistrationProxy<>(new TextCommandExceptionRegistration());
    this.embeds = new RecordsRegistrationProxy<>(new MessageEmbedBuilderRegistration());
    this.arguments = new RecordsRegistrationProxy<>(arguments);
    this.textCommands = new RecordsRegistrationProxy<>(new TextCommandRegistration(arguments));
    factories.put(Extension.class, o -> this.extensions.register((Extension)o));
    factories.put(CommandException.class, o -> this.textCommandExceptions.register((CommandException<?>)o));
    factories.put(MessageEmbedBuilder.class, o -> this.embeds.register((MessageEmbedBuilder<?>)o));
    factories.put(ObjectParser.class, o -> this.arguments.register((ObjectParser<?>)o));
    factories.put(Command.class, o -> this.textCommands.register((Command<?>)o));
  }

  @Override
  public Consumer<Object> create(Class<?> clazz) {
    return factories.get(clazz);
  }

  @Override
  public void prepare(DependenceInjection injector) {
    extensions.prepare(injector);
    extensions.writeAll(injector);
    embeds.writeAll(injector);
    arguments.writeAll(injector);
    textCommandExceptions.writeAll(injector);
    textCommands.writeAll(injector);
  }

  @Override
  public ApplicationRegistries flush() {
    /* Registro de todos os bounders, comandos e parsers. Chamado quando a aplicação está consolidada. */
    extensions.flush();
    embeds.flush();
    arguments.flush();
    textCommandExceptions.flush();
    textCommands.flush();
    return new ApplicationRegistries(
      this.extensions,
      this.embeds,
      this.arguments,
      this.textCommandExceptions,
      this.textCommands
    );
  }

  public void clear() {
    extensions.clear();
    embeds.clear();
    arguments.clear();
    textCommandExceptions.clear();
    textCommands.clear();
  }

  @Override
  public void shutdown() {
    this.clear();
  }
}
