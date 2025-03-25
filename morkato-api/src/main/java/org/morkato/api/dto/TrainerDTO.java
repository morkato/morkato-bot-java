package org.morkato.api.dto;

import org.morkato.api.repository.trainer.TrainerCreationQuery;
import org.morkato.api.validation.constraints.MorkatoModelAttribute;
import org.morkato.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.api.validation.constraints.MorkatoModelBanner;
import org.morkato.api.validation.constraints.MorkatoModelName;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnUpdate;
import org.morkato.api.validation.groups.OnId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.groups.Default;
import jakarta.validation.Validator;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, of = {"guildId","id"})
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class TrainerDTO extends DefaultDTO<TrainerDTO> {
  @NotNull(groups = {Default.class, OnCreate.class, OnId.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnId.class})
  private String guildId;
  @NotNull(groups = {Default.class, OnId.class})
  @MorkatoSnowflakeId(groups = {Default.class, OnCreate.class, OnId.class})
  private String id;
  @NotNull(groups = {Default.class, OnCreate.class})
  @MorkatoModelName(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String name;
  @NotNull(groups = {Default.class})
  private long cooldown;
  @NotNull(groups = {Default.class})
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal life;
  @NotNull(groups = {Default.class})
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal breath;
  @NotNull(groups = {Default.class})
  @MorkatoModelAttribute(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private BigDecimal blood;
  private String emoji;
  @MorkatoModelBanner(groups = {Default.class, OnCreate.class, OnUpdate.class})
  private String banner;
  @NotNull(groups = {Default.class})
  private long xp;

  public static TrainerDTO from(TrainerCreationQuery query) {
    return new TrainerDTO()
      .setGuildId(query.guildId())
      .setName(query.name())
      .setCooldown(query.cooldown())
      .setLife(query.life())
      .setBreath(query.breath())
      .setBlood(query.blood())
      .setEmoji(query.emoji())
      .setBanner(query.banner())
      .setXp(query.xp());
  }

  @Override
  public Set<ConstraintViolation<TrainerDTO>> safeValidate(Validator validator, Class<?>... classes) {
    return validator.validate(this, classes);
  }
}