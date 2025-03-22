package org.morkato.bmt.registration;

import org.morkato.utility.ClassInjectorMap;
import org.morkato.utility.exception.InjectionException;
import org.morkato.utility.exception.ValueNotInjected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RecordsRegistrationProxy<T, O extends RegisterManagement<T>> implements RegisterManagement<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RecordsRegistrationProxy.class);
  private final Set<T> records = new HashSet<>();
  private boolean prepared = false;
  private final O management;

  public RecordsRegistrationProxy(O management) {
    this.management = management;
  }

  public O getOriginal() {
    return management;
  }

  @Override
  public void register(T registry) {
    records.add(registry);
  }

  @Override
  public void clear() {
    records.clear();
    management.clear();
  }

  @SuppressWarnings("unchecked")
  public void prepare(ClassInjectorMap injector) {
    final Set<T> validatedRecords = new HashSet<>();
    if (Objects.nonNull(injector)) {
      injector.injectAllIfAbsent((Set<Object>)records);
      for (T record : records) {
        try {
          injector.write(record);
          validatedRecords.add(record);
        } catch (InjectionException exc) {
          LOGGER.warn("Error to finalize record injection: {}. An unexpected injection error occurred: {}.", record.getClass().getName(), exc.getMessage());
        } catch (ValueNotInjected exc) {
          LOGGER.warn("Error to finalize record injection: {}. Value: {} is not injected.", record.getClass().getName(), exc.getType());
        }
      }
    }
    records.retainAll(validatedRecords);
    validatedRecords.clear();
    this.prepared= true;
  }

  public void flush() {
    if (!prepared)
      LOGGER.warn("Records has not been finalized! This may generate unwanted behavior.");
    if (records.isEmpty())
      return;
    RegisterManagement.registerAll(management, records);
    management.flush();
    records.clear();
  }
}
