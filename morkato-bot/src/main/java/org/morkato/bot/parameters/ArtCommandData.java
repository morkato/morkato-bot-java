package org.morkato.bot.parameters;

import org.morkato.bmt.annotation.NotRequired;

public record ArtCommandData(
  @NotRequired
  ArtOption option,
  String query
) {
}
