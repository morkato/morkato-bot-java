package org.morkato.bmt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.loader.ComponentLoader;
import org.morkato.bmt.registration.RegistrationFactory;
import org.morkato.utility.exception.InjectionException;
import org.morkato.bmt.exception.ValueNotInjected;
import org.reflections.Reflections;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class ApplicationCommon extends ApplicationBot<ApplicationRegistries> {
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
  protected RegistrationFactory<ApplicationRegistries> createFactory() throws Throwable {
    return RegistrationFactory.createDefault();
  }

  @Override
  protected DependenceInjection createDependenceInjection(){
    final DependenceInjection injector = DependenceInjection.createDefault();
    for (String name : properties.stringPropertyNames()) {
      injector.setProperty(name, properties.getProperty(name));
    }
    return injector;
  }

  @Override
  protected void instance(ComponentLoader loader) {
    final Reflections reflections = new Reflections(mainClazz.getPackageName(), SubTypes, TypesAnnotated);
    final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
    for (Class<?> clazz : classes) {
      try {
        loader.instance(clazz);
        /* Grub, component initialized. */
      } catch (InjectionException exc) {
        LOGGER.warn("Error to loading component: {}. An unexpected injection error occurred: {}.", clazz.getName(), exc.getMessage());
      } catch (ValueNotInjected exc) {
        LOGGER.warn("Error to loading component: {}. Value: {} is not injected.", clazz.getName(), exc.getType());
      } catch (NoSuchMethodException exc) {
        LOGGER.warn("Error loading component: {}. No default constructor found. Ignoring.", clazz.getName());
      } catch (ReflectiveOperationException exc) {
        LOGGER.warn("Error loading component: {}. Unexpected reflection error: {}. Ignoring.", clazz.getName(), exc.getMessage());
      } catch (Throwable exc) {
        /* RuntimeExceptions */
        LOGGER.error("Error on loading component: {}. An unexpected error occurred: {}. Ignoring.", clazz.getName(), exc.getClass(), exc);
      }
    }
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
  protected void onReady(JDA jda, ApplicationRegistries registries) {
    LOGGER.info("All components have been initialized successfully.");
    LOGGER.info("Totally components initialized: {} (Including JDA).", registries.totally());
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
