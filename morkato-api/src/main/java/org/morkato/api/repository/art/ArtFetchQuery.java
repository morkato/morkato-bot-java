package org.morkato.api.repository.art;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.morkato.api.entity.art.ArtId;

@AllArgsConstructor
@Getter
public class ArtFetchQuery implements ArtId {
  private String guildId;
  private String id;
}
