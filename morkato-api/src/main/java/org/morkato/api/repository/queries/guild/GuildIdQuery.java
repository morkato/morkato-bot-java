package org.morkato.api.repository.queries.guild;

import org.morkato.api.entity.guild.GuildId;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GuildIdQuery implements GuildId {
  private String id;
}
