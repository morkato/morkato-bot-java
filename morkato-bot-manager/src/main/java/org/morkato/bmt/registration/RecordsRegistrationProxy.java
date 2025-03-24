package org.morkato.bmt.registration;

import org.morkato.bmt.DependenceInjection;
import org.morkato.utility.exception.InjectionException;
import org.morkato.bmt.exception.ValueNotInjected;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.*;

public class RecordsRegistrationProxy<O extends RegisterManagement<T>, T> implements RegisterManagement<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(RecordsRegistrationProxy.class);
  private final O management;
  protected final Set<T> records = new HashSet<>();
  private boolean prepared = false;
  private boolean flushed = false;

  public RecordsRegistrationProxy(O management) {
    this.management = management;
  }

  public O getManagement() {
    return this.management;
  }

  public void retainsAll(Collection<T> records) {
    this.records.retainAll(records);
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
  @Nonnull
  public Iterator<T> iterator() {
    if (!this.flushed)
      throw new IllegalStateException("Records are not flushed");
    return this.management.iterator();
  }

  @Override
  public int size() {
    if (!this.flushed)
      throw new IllegalStateException("Records are not flushed");
    return this.management.size();
  }

  @SuppressWarnings("unchecked")
  public void prepare(DependenceInjection injector) {
    final Set<T> validatedRecords = new HashSet<>();
    if (Objects.nonNull(injector)) {
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
      injector.injectAllIfAbsent((Set<Object>)validatedRecords);
    }
    records.retainAll(validatedRecords);
    validatedRecords.clear();
    this.prepared = true;
  }

  @Override
  public void flush() {
    if (!prepared)
      LOGGER.warn("Records has not been finalized! This may generate unwanted behavior.");
    if (records.isEmpty())
      return;
    RegisterManagement.registerAll(management, records);
    management.flush();
    records.clear();
    this.flushed = true;
  }
}
