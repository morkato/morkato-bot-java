package org.morkato.api.repository.attack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.morkato.api.entity.attack.AttackId;

@AllArgsConstructor
@Getter
public class AttackIdQuery implements AttackId {
  private String guildId;
  private String id;
}
