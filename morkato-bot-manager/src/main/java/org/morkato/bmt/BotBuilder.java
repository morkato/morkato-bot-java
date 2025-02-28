package org.morkato.bmt;

import org.morkato.bmt.commands.CommandExecutor;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class BotBuilder {
  private final Logger logger = LoggerFactory.getLogger(BotBuilder.class);
  private final String token;

  public BotBuilder(
    @NotNull String token
  ) {
    this.token = token;
  }

  public JDA build(CommandExecutor executor) throws Exception {
    JDABuilder builder = JDABuilder.createDefault(token);
    BotListener baseListener = new BotListener(executor);
    builder.addEventListeners(baseListener);
    return builder
      .enableIntents(GatewayIntent.MESSAGE_CONTENT)
      .build()
      .awaitReady();
  }
}
