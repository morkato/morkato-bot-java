package org.morkato.api.entity.impl.attack;

import org.morkato.api.dto.AttackDTO;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.attack.AttackFlags;
import org.morkato.api.entity.attack.AttackUpdateBuilder;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.art.Art;
import org.morkato.api.repository.AttackRepository;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.repository.attack.AttackUpdateQuery;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;

public class ApiAttackImpl implements Attack{
  private final RepositoryCentral central;
  private final Guild guild;
  private final Art art;
  private String guildId;
  private String id;
  private String artId;
  private String name;
  private String prefix;
  private String description;
  private String banner;
  private BigDecimal poisonTurn;
  private BigDecimal poison;
  private BigDecimal damage;
  private BigDecimal breath;
  private BigDecimal blood;
  private AttackFlags flags;

  public ApiAttackImpl(
    @Nonnull RepositoryCentral central,
    @Nonnull Art art,
    @Nonnull AttackDTO dto
  ) {
    this.central = central;
    this.guild = art.getGuild();
    this.art = art;
    if (!art.getId().equals(dto.getArtId()) || !guild.getId().equals(dto.getGuildId()))
      /* TODO: Impossible error, but it's a prevent for stupid scenery. Add a custom error for this */
      throw new RuntimeException("");
    this.fromDTO(dto);
  }

  private void fromDTO(AttackDTO dto){
    this.guildId = dto.getGuildId();
    this.id = dto.getId();
    this.artId = dto.getArtId();
    this.name = dto.getName();
    this.prefix = dto.getPrefix();
    this.description = dto.getDescription();
    this.banner = dto.getBanner();
    this.poisonTurn = dto.getPoisonTurn();
    this.poison = dto.getPoison();
    this.damage = dto.getDamage();
    this.breath = dto.getBreath();
    this.blood = dto.getBlood();
    this.flags = AttackFlags.of(dto.getFlags());
  }

  @Nonnull
  @Override
  public String getGuildId() {
    return this.guildId;
  }

  @Nonnull
  @Override
  public String getId() {
    return this.id;
  }

  @Nonnull
  @Override
  public String getArtId() {
    return this.artId;
  }

  @Nonnull
  @Override
  public Guild getGuild() {
    return this.guild;
  }

  @Nonnull
  @Override
  public Art getArt() {
    return this.art;
  }

  @Nonnull
  @Override
  public String getName() {
    return this.name;
  }

  @Nullable
  @Override
  public String getPrefix() {
    return this.prefix;
  }

  @Nullable
  @Override
  public String getDescription() {
    return this.description;
  }

  @Nullable
  @Override
  public String getBanner() {
    return this.banner;
  }

  @Nonnull
  @Override
  public BigDecimal getPoisonTurn() {
    return this.poisonTurn;
  }

  @Nonnull
  @Override
  public BigDecimal getPoison() {
    return this.poison;
  }

  @Nonnull
  @Override
  public BigDecimal getDamage() {
    return this.damage;
  }

  @Nonnull
  @Override
  public BigDecimal getBreath() {
    return this.breath;
  }

  @Nonnull
  @Override
  public BigDecimal getBlood() {
    return this.blood;
  }

  @Nonnull
  @Override
  public AttackFlags getFlags() {
    return this.flags;
  }

  @Override
  public AttackUpdateBuilder doUpdate() {
    return new ApiAttackUpdateBuilderImpl(this);
  }

  @Override
  public void update(AttackUpdateQuery query) {
    AttackRepository attacks = central.attack();
    attacks.update(query);
  }

  @Override
  public Attack delete() {
    AttackRepository attacks = central.attack();
    attacks.delete(this);
    return this;
  }
}
