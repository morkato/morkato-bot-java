package org.morkato.bmt.bmt.components;

import org.morkato.bmt.bmt.context.TextCommandContext;

public interface CommandException<T extends Throwable> {
  void doException(TextCommandContext<?> context,T exception);
}
