package org.morkato.bmt.bot.parameters;

import org.morkato.bmt.bmt.annotation.NotRequired;

public record ArtCommandData(
  @NotRequired
  ArtOption option
//  Art art
) {
}
