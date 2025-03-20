package org.morkato.bmt.api.entity.trainer;

import org.morkato.bmt.api.entity.ApiObject;
import org.morkato.bmt.api.entity.impl.trainer.TrainerImpl;
import org.morkato.bmt.api.entity.guild.Guild;
import org.morkato.bmt.api.dto.TrainerDTO;
import org.morkato.bmt.api.DeleteApiModel;
import org.morkato.bmt.api.UpdateApiModel;
import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface Trainer extends TrainerId, ApiObject, UpdateApiModel<Trainer, TrainerUpdateBuilder>, DeleteApiModel<Trainer> {
  static String representation(Trainer trainer) {
    return "Trainer[guild = " + trainer.getGuild() + ", id = " + trainer.getId() + ", name = " + trainer.getName() + "]";
  }
  static Trainer createDefault(
    @Nonnull Guild guild,
    @Nonnull TrainerDTO dto
  ) {
    return new TrainerImpl(guild, dto);
  }
  Guild getGuild();
  long getCooldown();
  int getCooldownTimeSeconds();
  int getCooldownPeer();
  @Nonnull
  BigDecimal getLife();
  @Nonnull
  BigDecimal getBreath();
  @Nonnull
  BigDecimal getBlood();
  String getEmoji();
  @Nonnull
  String getBanner();
  long getXpChunk();
  @Nonnull
  BigDecimal getMaxXp();
  @Nonnull
  BigDecimal getMinXp();
  @Nonnull
  BigDecimal getSortedXp();
}
