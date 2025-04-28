package org.morkato.bmt.registration;

import org.morkato.boot.exception.ValueNotInjected;
import org.morkato.boot.DependenceInjection;
import org.morkato.boot.exception.InjectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class RegistrationInterceptor<P, T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationInterceptor.class);
  protected final Set<P> records = new HashSet<>();
  private final RegisterManagement<P, T> management;
  private final DependenceInjection injector;
  private boolean computed = false;

  public RegistrationInterceptor(RegisterManagement<P, T> management, DependenceInjection injector) {
    this.management = Objects.requireNonNull(management);
    this.injector = Objects.requireNonNull(injector);
  }

  public void register(P registry) {
    if (computed) {
      LOGGER.warn("Interceptor has been computed. Not append a registry. Ignoring.");
      return;
    }
    records.add(registry);
  }

  public RegisterManagement<P, T> compute() {
    if (computed)
      return management;
    for (P record : records) {
      try {
        injector.writeProperties(record);
        injector.write(record);
        management.register(record);
        injector.injectIfAbsent(record);
      } catch (ValueNotInjected exc) {
        LOGGER.warn("Error to finalize record injection: {}. Value: {} is not injected. Extension has been dropped.", record.getClass().getName(), exc.getType());
      } catch (InjectionException exc) {
        LOGGER.error("Error to finalize record injection: {}. An unexpected error occurred:", record.getClass().getName(), exc);
      } catch (Throwable exc) {
        LOGGER.error("Failed to finalize record injection: {}. An critical unexpected error occurred:", record.getClass().getName(), exc);
      }
    }
    management.flush();
    computed = true;
    return management;
  }
}