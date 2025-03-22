package org.morkato.bmt;

import org.morkato.bmt.components.Command;

public record SubCommandPayload(
  Class<? extends Command<?>> dad,
  Class<? extends Command<?>> children,
  String pointer
) {
}
