package org.morkato.bmt.bmt.components;

import org.morkato.bmt.bmt.registration.ArgumentRegistration;
import org.morkato.bmt.bmt.context.TextCommandContext;

public interface ObjectParser<T> {
  T parse(TextCommandContext<?> context, String text) throws Throwable;
  default void flush(ArgumentRegistration registration) throws Throwable {}
}
