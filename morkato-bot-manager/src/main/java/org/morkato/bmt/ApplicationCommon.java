package org.morkato.bmt;

import org.morkato.bmt.startup.BotRegistrationFactory;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.Extension;
import net.dv8tion.jda.api.JDA;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public abstract class ApplicationCommon extends ApplicationBot {
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

  @Override
  protected void onReady(JDA jda, BotCore core) {
    core.syncSlashCommands(jda);
    LOGGER.info("All components have been initialized successfully.");
    LOGGER.info("Total of {} text parsers, {} slash mappers, {} text commands, {} slash commands and {} actions loaded successfully.",
      core.getParsers().getRegisteredObjectParserLength(), core.getParsers().getRegisteredSlashMapperLength(),
      core.getCommands().getRegisteredTextCommandsLength(), core.getCommands().getRegisteredSlashCommandsLength(),
      core.getActions().getRegisteredActionsLength());
    LOGGER.info("Estou conectado, como: {} (id={})", jda.getSelfUser().getAsTag(), jda.getSelfUser().getId());
  }

  @Override
  protected void close() {
    LOGGER.info("Shutting down application and cleaning up resources.");
    /* Invocado para fechamento: SIGTERM etc... Pode fechar processador de comandos (Com threads alugadas, eventloops, etc...) */
  }

  @Override
  public void run() throws Exception {
    LOGGER.info("Launching bot: [{}] PID: {}", mainClazz.getSimpleName(), ManagementFactory.getRuntimeMXBean().getPid());
    LOGGER.info("Creating a context to run discord bot ({} -- {}).", ApplicationCommon.class.getPackageName(), mainClazz.getPackageName());
    super.run();
  }
}
