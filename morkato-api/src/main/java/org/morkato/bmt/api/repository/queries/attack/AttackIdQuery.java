package org.morkato.bmt.api.repository.queries.attack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.morkato.bmt.api.entity.attack.AttackId;

@AllArgsConstructor
@Getter
public class AttackIdQuery implements AttackId {
  private String guildId;
  private String id;
}
