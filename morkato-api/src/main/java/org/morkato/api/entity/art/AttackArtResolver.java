package org.morkato.api.entity.art;

import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.impl.ObjectResolverImpl;

import java.util.Objects;

public class AttackArtResolver
  extends ObjectResolverImpl<Attack>
  implements ObjectResolver<Attack> {
  private final Art art;
  public AttackArtResolver(Art art) {
    Objects.requireNonNull(art);
    this.art = art;
  }
  @Override
  protected void resolveImpl() {
    final Guild guild = art.getGuild();
    final ObjectResolver<Attack> guildAttacks = guild.getAttackResolver();
    guildAttacks.resolve();
  }
}
