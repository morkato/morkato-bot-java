package org.morkato.api.repository.art;

import org.morkato.api.entity.art.ArtId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ArtIdQuery implements ArtId {
  private final String guildId;
  private final String id;

  public ArtIdQuery(String guildId, String id) {
    this.guildId = Objects.requireNonNull(guildId);
    this.id = Objects.requireNonNull(id);
  }
}
