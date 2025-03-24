package org.morkato.api.entity.guild;

import org.morkato.api.dto.AttackDTO;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.impl.ObjectResolverImpl;
import org.morkato.api.entity.impl.attack.ApiAttackImpl;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.repository.queries.attack.AttackFetchAllQuery;

import java.util.Objects;

public class GuildAttackResolver
  extends ObjectResolverImpl<Attack>
  implements ObjectResolver<Attack> {
  private final RepositoryCentral central;
  private final Guild guild;
  public GuildAttackResolver(Guild guild, RepositoryCentral central) {
    Objects.requireNonNull(central);
    Objects.requireNonNull(guild);
    this.central = central;
    this.guild = guild;
  }

  @Override
  protected void resolveImpl() {
    final ObjectResolver<Art> arts = this.guild.getArtResolver();
    final AttackDTO[] dtos = central.attack().fetchAll(new AttackFetchAllQuery(this.guild.getId()));
    arts.resolve();
    for (AttackDTO dto : dtos) {
      this.add(new ApiAttackImpl(central, arts.get(dto.getArtId()), dto));
    }
  }
}
