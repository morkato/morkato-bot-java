package org.morkato.bmt;

import org.morkato.bmt.context.BotContext;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.morkato.bmt.generated.ApplicationStaticRegistries;
import org.morkato.bmt.internal.registration.BotRegistrationFactory;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.Extension;
import org.morkato.boot.ExtensionManager;

import java.util.Collection;
import java.util.Objects;

public abstract class ApplicationBot extends Application<ApplicationStaticRegistries> {
  protected abstract BotRegistrationFactory createFactory(JDA jda, DependenceInjection injector) throws Throwable;
  protected abstract Collection<Extension<BotContext>> createExtensions();
  protected abstract DependenceInjection createDependenceInjection();
  protected abstract Collection<ListenerAdapter> createListeners();
  protected abstract Collection<GatewayIntent> createIntents();
  protected abstract String getToken();

  @Override
  protected JDA getJDA() {
    final String token = Objects.requireNonNull(this.getToken());
    final Collection<ListenerAdapter> adapters = this.createListeners();
    final Collection<GatewayIntent> intents = this.createIntents();
    JDABuilder builder = JDABuilder.createDefault(token);
    for (GatewayIntent intent : intents) {
      builder.enableIntents(intent);
    }
    for (ListenerAdapter adapter : adapters) {
      builder.addEventListeners(adapter);
    }
    return builder.build();
  }

  @Override
  protected ApplicationStaticRegistries bootstrap(JDA jda) throws Throwable {
    LOGGER.info("Initialize bootstrap.");
    Objects.requireNonNull(jda);
    final DependenceInjection injector = this.createDependenceInjection();
    final ExtensionManager<BotContext> extensions = new ExtensionManager<>(injector);
    final BotRegistrationFactory factory = this.createFactory(jda, injector);
    final BotContextFactory ctxfactory = new BotContextFactory(factory);
    for (Extension<BotContext> extension : this.createExtensions())
      extensions.instance(extension);
    extensions.start();
    injector.injectIfAbsent(jda);
    injector.injectIfAbsent(jda.getSelfUser());
    LOGGER.info("Setup all extensions");
    extensions.setup(ctxfactory);
    LOGGER.info("Flushing component registration to finalize component registration.");
    return factory.flush();
  }
}
