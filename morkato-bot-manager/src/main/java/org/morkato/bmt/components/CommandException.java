package org.morkato.bmt.components;

import org.morkato.bmt.context.TextCommandContext;

public interface CommandException<T extends Throwable> {
  void doException(TextCommandContext<?> context,T exception);
}
