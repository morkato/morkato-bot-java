package org.morkato.bmt.api.repository.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.morkato.bmt.api.entity.art.ArtId;

@AllArgsConstructor
@Getter
public class ArtFetchQuery implements ArtId {
  private String guildId;
  private String id;
}
