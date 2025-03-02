package org.morkato.api.entity.attack;

import org.morkato.api.DeleteApiModel;
import org.morkato.api.UpdateApiModel;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.guild.Guild;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;

public interface Attack extends AttackId, UpdateApiModel<Attack, AttackUpdateBuilder>, DeleteApiModel<Attack>{
  @Nonnull
  String getArtId();
  @Nonnull
  Guild getGuild();
  @Nonnull
  Art getArt();
  @Nonnull
  String getName();
  @Nullable
  String getPrefix();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
  @Nonnull
  BigDecimal getPoisonTurn();
  @Nonnull
  BigDecimal getPoison();
  @Nonnull
  BigDecimal getDamage();
  @Nonnull
  BigDecimal getBreath();
  @Nonnull
  BigDecimal getBlood();
  @Nonnull
  AttackFlags getFlags();
}
