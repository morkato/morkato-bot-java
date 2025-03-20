package org.morkato.bmt.api.entity.attack;

import org.morkato.bmt.api.DeleteApiModel;
import org.morkato.bmt.api.UpdateApiModel;
import org.morkato.bmt.api.entity.ApiObject;
import org.morkato.bmt.api.entity.art.Art;
import org.morkato.bmt.api.entity.guild.Guild;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;

public interface Attack extends AttackId, ApiObject, UpdateApiModel<Attack, AttackUpdateBuilder>, DeleteApiModel<Attack>{
  @Nonnull
  String getArtId();
  @Nonnull
  Guild getGuild();
  @Nonnull
  Art getArt();
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
