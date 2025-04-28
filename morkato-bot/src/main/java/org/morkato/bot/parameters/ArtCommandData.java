package org.morkato.bot.parameters;

import org.morkato.boot.annotation.NotRequired;

public record ArtCommandData(
  @NotRequired
  ArtOption option,
  String query
) {
}
