package org.morkato.mcbmt;

import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class Application<T extends Shutdownable> {
  protected abstract JDA getJDA();
  protected abstract void onReady(JDA jda, T managment) throws Exception;
  protected abstract T bootstrap(JDA jda) throws Exception;
  protected abstract void close();

  protected final Logger LOGGER = LoggerFactory.getLogger(Application.class);
  private T managment;
  private JDA jda;

  public Application() {
    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
  }

  public void run() throws Exception {
    try {
      jda = Objects.requireNonNull(this.getJDA());
      managment = Objects.requireNonNull(this.bootstrap(jda));
      jda.awaitReady();
      this.onReady(jda, managment);
    } catch (Exception exc) {
      this.shutdown();
      throw exc;
    }
  }

  public void shutdown() {
    if (Objects.nonNull(jda))
      jda.shutdownNow();
    if (Objects.nonNull(managment))
      managment.shutdown();
    this.close();
  }
}
