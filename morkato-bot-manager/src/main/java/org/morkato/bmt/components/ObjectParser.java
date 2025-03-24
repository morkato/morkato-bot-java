package org.morkato.bmt.components;

import org.morkato.bmt.context.TextCommandContext;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.registration.hooks.ArgumentRegistration;

public interface ObjectParser<T> {
  T parse(TextCommandContext<?> context, String text) throws Throwable;
  default void flush(ArgumentRegistration registries) throws Throwable {}
}
