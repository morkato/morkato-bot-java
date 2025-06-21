package org.morkato.bmt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.generated.ApplicationStaticRegistries;
import org.morkato.bmt.startup.BotRegistrationFactory;
import org.morkato.bmt.generated.registries.SlashCommandRegistry;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.Extension;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class ApplicationCommon extends ApplicationBot {
  private final Class<?> mainClazz;
  private final Properties properties;
  private final String token;

  public ApplicationCommon(Class<?> mainClazz, String token, Properties properties) {
    super();
    this.mainClazz = Objects.requireNonNull(mainClazz);
    this.token = Objects.requireNonNull(token);
    this.properties = Objects.requireNonNull(properties);
  }

  @Override
  protected BotRegistrationFactory createFactory(JDA jda, DependenceInjection injector) {
    return new BotRegistrationFactory(injector);
  }

  @Override
  protected DependenceInjection createDependenceInjection() {
    final DependenceInjection injector = DependenceInjection.createDefault();
    for (String name : properties.stringPropertyNames()) {
      injector.setProperty(name, properties.getProperty(name));
    }
    return injector;
  }

  protected Collection<Extension<BotContext>> createExtensions() {
    return Set.of();
  }

  @Override
  protected Collection<ListenerAdapter> createListeners() {
    return Set.of();
  }

  @Override
  protected Collection<GatewayIntent> createIntents() {
    return Set.of();
  }

  @Override
  protected String getToken() {
    return token;
  }

  protected void syncSlashCommands(JDA jda, ApplicationStaticRegistries registries) {
    LOGGER.info("Preparing to sync slashcommands with discord app.");
    CommandListUpdateAction action = jda.updateCommands();
    SlashCommandRegistry<?>[] slashcommands = registries.getCommands().getRegisteredSlashCommands();
    for (SlashCommandRegistry<?> slash : slashcommands) {
      LOGGER.debug("Prepare to slashcommand named: {}", slash.getName());
      action = action.addCommands(
        Commands.slash(slash.getName(), slash.getDescription() == null ? "..." : slash.getDescription())
          .addOptions(slash.getOptions())
      );
    }
    action.queue();
  }

  @Override
  protected void onReady(JDA jda, ApplicationStaticRegistries registries) {
    this.syncSlashCommands(jda, registries);
    LOGGER.info("All components have been initialized successfully.");
    LOGGER.info("Estou conectado, como: {} (id={})", jda.getSelfUser().getAsTag(), jda.getSelfUser().getId());
  }

  @Override
  protected void close() {
    LOGGER.info("Shutting down application and cleaning up resources.");
    /* Invocado para fechamento: SIGTERM etc... Pode fechar processador de comandos (Com threads alugadas, eventloops, etc...) */
  }

  @Override
  public void run() throws Throwable {
    LOGGER.info("Launching bot: [{}] PID: {}", mainClazz.getSimpleName(), ManagementFactory.getRuntimeMXBean().getPid());
    LOGGER.info("Creating a context to run discord bot ({} -- {}).", ApplicationCommon.class.getPackageName(), mainClazz.getPackageName());
    super.run();
  }
}
