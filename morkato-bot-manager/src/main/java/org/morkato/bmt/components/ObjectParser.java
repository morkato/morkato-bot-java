package org.morkato.bmt.components;

import org.morkato.bmt.registration.ArgumentRegistration;
import org.morkato.bmt.context.TextCommandContext;

public interface ObjectParser<T> {
  T parse(TextCommandContext<?> context, String text) throws Throwable;
  default void flush(ArgumentRegistration registration) throws Throwable {}
}
