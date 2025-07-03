package org.morkato.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.morkato.mcbmt.ApplicationCommon;
import org.morkato.mcbmt.BotCore;
import org.morkato.mcbmt.BotContext;
import org.morkato.mcbmt.invoker.ActionInvoker;
import org.morkato.mcbmt.invoker.SlashCommandInvoker;
import org.morkato.mcbmt.listener.ActionListener;
import org.morkato.mcbmt.listener.SlashCommandListener;
import org.morkato.boot.Extension;
import org.morkato.mcbmt.invoker.CommandInvoker;
import org.morkato.mcbmt.listener.TextCommandListener;
import org.morkato.bot.extension.RPGBaseExtension;
import org.morkato.mcisid.Mcisid;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Objects;
import java.util.Set;

public class Client extends ApplicationCommon {
  private static final String DEFAULT_PROPERTY = "morkato.conf";
  private final CommandInvoker textinvoker= new CommandInvoker();
  private final SlashCommandInvoker slashinvoker = new SlashCommandInvoker();
  private final ActionInvoker actioninvoker = new ActionInvoker();

  public static Properties load(String sysProperty) throws IOException {
    Properties properties = new Properties();
    String fileconf = System.getProperty(sysProperty);
    if (fileconf == null)
      /* TODO: Moving this */
      throw new RuntimeException(sysProperty);
    InputStream stream = new FileInputStream(fileconf);
    properties.load(stream);
    stream.close();
    return properties;
  }

  public static Properties loadDefault() throws IOException {
    return load(DEFAULT_PROPERTY);
  }

  public Client(Properties properties, String token) {
    super(Client.class, token, properties);
  }

  public static void main(String[] args) throws Throwable {
    Mcisid.ensureLoadLib();
    final Properties properties = loadDefault();
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
      new RPGBaseExtension()
    );
  }

  @Override
  protected Collection<ListenerAdapter> createListeners() {
    return Set.of(
      new TextCommandListener(textinvoker) {
        @Override
        public String getPrefix(){
          return "!!";
        }
      },
      new SlashCommandListener(slashinvoker),
      new ActionListener(actioninvoker)
    );
  }

  @Override
  protected Collection<GatewayIntent> createIntents() {
    return Set.of(
      GatewayIntent.MESSAGE_CONTENT
    );
  }

  @Override
  protected void onReady(JDA jda, BotCore core) {
    super.onReady(jda, core);
    core.invoke(textinvoker);
    core.invoke(slashinvoker);
    core.invoke(actioninvoker);
  }
}
