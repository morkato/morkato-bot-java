package org.morkato.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.morkato.bmt.ApplicationRegistries;
import org.morkato.bmt.invoker.CommandInvoker;
import org.morkato.bmt.listener.TextCommandListener;
import org.morkato.utility.MorkatoConfigLoader;
import org.morkato.bmt.ApplicationCommon;
import java.util.Collection;
import java.util.Properties;
import java.util.Objects;
import java.util.Set;

public class Client extends ApplicationCommon {
  private final CommandInvoker invoker = new CommandInvoker();

  public Client(Properties properties, String token) {
    super(Client.class, token, properties);
    invoker.setDebug(true);
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
  protected Collection<ListenerAdapter> createListeners() {
    return Set.of(
      new TextCommandListener(invoker) {
        @Override
        public String getPrefix(){
          return "!!";
        }
      }
    );
  }

  @Override
  protected Collection<GatewayIntent> createIntents() {
    return Set.of(
      GatewayIntent.MESSAGE_CONTENT
    );
  }

  @Override
  protected void onReady(JDA jda, ApplicationRegistries registries) {
    super.onReady(jda, registries);
    invoker.start(
      new CommandsStaticRegistries(registries),
      new CommandExceptionStaticRegistries(registries)
    );
  }

  @Override
  protected void close() {
    super.close();
    invoker.shutdown();
  }
}
