package org.morkato.bmt.registration;

import org.morkato.bmt.DependenceInjection;
import org.morkato.bmt.registration.registries.Registry;
import org.morkato.utility.exception.InjectionException;
import org.morkato.bmt.exception.ValueNotInjected;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.*;

public class RecordsRegistrationProxy<R extends Registry<T>, T> implements RegisterManagement<R, T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RecordsRegistrationProxy.class);
  private final RegisterManagement<R, T> management;
  protected final Set<T> records = new HashSet<>();
  private boolean injected= false;
  private boolean flushed = false;

  public RecordsRegistrationProxy(RegisterManagement<R, T> management) {
    this.management = management;
  }

  public RegisterManagement<R, T> getManagement() {
    return this.management;
  }

  public void retainsAll(Collection<T> records) {
    this.records.retainAll(records);
  }

  @Override
  public Collection<R> registries() {
    if (!this.flushed)
      throw new IllegalStateException("Records are not flushed");
    return management.registries();
  }

  @Override
  public Collection<T> items() {
    if (!this.flushed)
      throw new IllegalStateException("Records are not flushed");
    return management.items();
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

  @Override
  public int size() {
    if (!this.flushed)
      throw new IllegalStateException("Records are not flushed");
    return this.management.size();
  }

  @SuppressWarnings("unchecked")
  public void writeAll(DependenceInjection injector) {
    final Set<T> validatedRecords = new HashSet<>();
    if (Objects.nonNull(injector)) {
      for (T record : records) {
        try {
          injector.writeProperties(record);
          injector.write(record);
          validatedRecords.add(record);
        } catch (InjectionException exc) {
          LOGGER.warn("Error to finalize record injection: {}. An unexpected injection error occurred: {}.", record.getClass().getName(), exc.getMessage());
        } catch (ValueNotInjected exc) {
          LOGGER.warn("Error to finalize record injection: {}. Value: {} is not injected.", record.getClass().getName(), exc.getType());
        } catch (Throwable exc) {
          LOGGER.warn("Error to finalize record injection: {}. An unexpected error occurred:", record.getClass().getName(), exc);
        }
      }
    }
    injector.injectAllIfAbsent((Set<Object>)validatedRecords);
    records.retainAll(validatedRecords);
    validatedRecords.clear();
    this.injected = true;
  }

  @Override
  public void flush() {
    if (!injected)
      LOGGER.warn("Records has not been finalized! This may generate unwanted behavior.");
    if (flushed)
      return;
    RegisterManagement.registerAll(management, records);
    management.flush();
    records.clear();
    this.flushed = true;
  }
}
