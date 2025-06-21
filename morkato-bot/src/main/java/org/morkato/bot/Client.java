package org.morkato.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.morkato.bmt.ApplicationCommon;
import org.morkato.bmt.generated.ApplicationStaticRegistries;
import org.morkato.bmt.context.BotContext;
import org.morkato.bmt.generated.CommandsStaticRegistries;
import org.morkato.bmt.invoker.SlashCommandInvoker;
import org.morkato.bmt.listener.SlashCommandListener;
import org.morkato.boot.Extension;
import org.morkato.bmt.invoker.CommandInvoker;
import org.morkato.bmt.listener.TextCommandListener;
import org.morkato.bot.extension.MorkatoAPIExtension;
import org.morkato.bot.extension.RPGBaseExtension;
import org.morkato.utility.MorkatoConfigLoader;
import java.util.Collection;
import java.util.Properties;
import java.util.Objects;
import java.util.Set;

public class Client extends ApplicationCommon {
  private final CommandInvoker invoker = new CommandInvoker();
  private final SlashCommandInvoker slashinvoker = new SlashCommandInvoker();

  public Client(Properties properties, String token) {
    super(Client.class, token, properties);
  }

  public static void main(String[] args) throws Throwable {
    final Properties properties = MorkatoConfigLoader.loadDefault();
    final String token = properties.getProperty("morkato.bot.token");
    Objects.requireNonNull(token, "morkato.bot.token is required to run bot!");
    properties.remove("morkato.bot.token");
    final Client client = new Client(properties, token);
    client.run();
    System.gc();
    /* Olá GC? Limpou? Não limpou? Por favor GC, limpe minha memória :/ */
  }

  @Override
  protected Collection<Extension<BotContext>> createExtensions() {
    return Set.of(
      new MorkatoAPIExtension(),
      new RPGBaseExtension()
    );
  }

  @Override
  protected Collection<ListenerAdapter> createListeners() {
    return Set.of(
      new TextCommandListener(invoker) {
        @Override
        public String getPrefix(){
          return "!!";
        }
      },
      new SlashCommandListener(slashinvoker)
    );
  }

  @Override
  protected Collection<GatewayIntent> createIntents() {
    return Set.of(
      GatewayIntent.MESSAGE_CONTENT
    );
  }

  @Override
  protected void onReady(JDA jda, ApplicationStaticRegistries registries) {
    super.onReady(jda, registries);
    invoker.start(
      registries.getCommands(),
      registries.getExceptions()
    );
    slashinvoker.start(
      registries.getCommands(),
      registries.getExceptions()
    );
  }

  @Override
  protected void close() {
    super.close();
    invoker.shutdown();
  }
}
