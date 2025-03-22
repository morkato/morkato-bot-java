package org.morkato.api.entity.impl.trainer;

import org.morkato.api.dto.TrainerDTO;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.repository.queries.attack.AttackUpdateQuery;
import org.morkato.api.entity.trainer.TrainerUpdateBuilder;
import org.morkato.api.entity.trainer.Trainer;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

public class TrainerImpl implements Trainer {
  private final Guild guild;
  private final String guildId;
  private final String id;
  private String name;
  private long cooldown;
  private BigDecimal life;
  private BigDecimal breath;
  private BigDecimal blood;
  private String emoji;
  private String banner;
  private long xp;

  public TrainerImpl(
    @Nonnull Guild guild,
    @Nonnull TrainerDTO dto
  ) {
    Objects.requireNonNull(guild);
    Objects.requireNonNull(dto);
    this.guild = guild;
    if (!guild.getId().equals(dto.getGuildId()))
      /* TODO: Add a custom error for stupid scenery. */
      throw new RuntimeException("Guild ID with guild don't match's.");
    this.guildId = dto.getGuildId();
    this.id = dto.getId();
    this.fromDTO(dto);
  }

  @Override
  public String toString() {
    return Trainer.representation(this);
  }

  public void fromDTO(TrainerDTO dto) {
    this.name = dto.getName();
    this.cooldown = dto.getCooldown();
    this.life = dto.getLife();
    this.breath = dto.getBreath();
    this.blood = dto.getBlood();
    this.emoji = dto.getEmoji();
    this.banner = dto.getBanner();
    this.xp = dto.getXp();
  }

  private int getXpExponent() {
    return Math.min((int)(xp & 0xFF), 18);
  }

  private BigDecimal getFullBase10Xp() {
    return BigDecimal.valueOf(Math.pow(10, this.getXpExponent()));
  }

  @Override
  public String getGuildId() {
    return guildId;
  }

  @Nonnull
  @Override
  public String getId() {
    return id;
  }

  @Override
  public Guild getGuild(){
    return guild;
  }

  @Nonnull
  @Override
  public String getName() {
    Objects.requireNonNull(name);
    return name;
  }

  @Override
  public long getCooldown() {
    return cooldown;
  }

  @Override
  public int getCooldownTimeSeconds() {
    return (int)(cooldown & 0xFFFF);
  }

  @Override
  public int getCooldownPeer() {
    return (int)((cooldown >> 16) & 0xFFFF);
  }

  @Nonnull
  @Override
  public BigDecimal getLife() {
    Objects.requireNonNull(life);
    return life;
  }

  @Nonnull
  @Override
  public BigDecimal getBreath() {
    Objects.requireNonNull(breath);
    return breath;
  }

  @Nonnull
  @Override
  public BigDecimal getBlood() {
    Objects.requireNonNull(blood);
    return blood;
  }

  @Override
  public String getEmoji() {
    return emoji;
  }

  @Nonnull
  @Override
  public String getBanner() {
    return banner;
  }

  @Override
  public long getXpChunk() {
    return xp;
  }

  @Nonnull
  @Override
  public BigDecimal getMaxXp() {
    return BigDecimal.valueOf((xp >> 16) & 0xFF)
      .multiply(this.getFullBase10Xp())
      .setScale(0, RoundingMode.HALF_DOWN);
  }

  @Nonnull
  @Override
  public BigDecimal getMinXp() {
    return BigDecimal.valueOf((xp >> 8) & 0xFF)
      .multiply(this.getFullBase10Xp())
      .setScale(0, RoundingMode.HALF_DOWN);
  }

  @Override
  @Nonnull
  public BigDecimal getSortedXp() {
    Random random = new Random();
    int min = (int)((xp >> 8) & 0xFF);
    int max = (int)((xp >> 16) & 0xFF);
    if (max <= min)
      return this.getMinXp();
    int generated = random.nextInt(max - min) + min;
    return BigDecimal.valueOf(generated)
      .multiply(this.getFullBase10Xp())
      .setScale(0, RoundingMode.HALF_DOWN);
  }

  @Override
  public Trainer delete() {
    throw new RuntimeException("Trainer::delete is not implemented in this implementation.");
  }

  @Override
  public TrainerUpdateBuilder doUpdate() {
    throw new RuntimeException("Trainer::doUpdate is not implemented in this implementation.");
  }

  @Override
  public void update(AttackUpdateQuery query) {
    throw new RuntimeException("Trainer::update is not implemented in this implementation.");
  }
}
