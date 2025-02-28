package org.morkato.api.repository.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArtFetchQuery {
  private String guildId;
  private String id;
}
