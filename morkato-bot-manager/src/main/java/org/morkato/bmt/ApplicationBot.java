package org.morkato.bmt;

import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.bmt.loader.ComponentLoader;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import java.util.Collection;
import java.util.Objects;

public abstract class ApplicationBot<T extends Shutdownable> extends Application<T> {
  protected abstract RegistrationFactory<T> createFactory() throws Throwable;
  protected abstract DependenceInjection createDependenceInjection();
  protected abstract void instance(ComponentLoader loader);
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
  protected T bootstrap(JDA jda) throws Throwable {
    LOGGER.info("Initialize bootstrap.");
    Objects.requireNonNull(jda);
    final DependenceInjection injector = this.createDependenceInjection();
    final RegistrationFactory<T> factory = this.createFactory();
    final ComponentLoader loader = new ComponentLoader(factory);
    injector.injectIfAbsent(jda);
    injector.injectIfAbsent(jda.getSelfUser());
    LOGGER.info("Flushing component registration to finalize component registration.");
    this.instance(loader);
    factory.prepare(injector);
    return factory.flush();
  }
}
