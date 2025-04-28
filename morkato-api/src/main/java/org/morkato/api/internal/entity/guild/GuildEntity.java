package org.morkato.api.internal.entity.guild;

import org.jetbrains.annotations.NotNull;
import org.morkato.api.ApiConnectionStatement;
import org.morkato.api.entity.ObjectResolver;
import org.morkato.api.entity.art.Art;
import org.morkato.api.entity.attack.Attack;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.entity.rpg.Rpg;
import org.morkato.api.entity.trainer.Trainer;

import java.util.Objects;

public class GuildEntity implements Guild {
  private final ApiConnectionStatement statement;
  private final String id;
  private final String rpgId;
  private String rollCategoryId;
  private String offCategoryId;
  private Rpg loadedRpg;

  public GuildEntity(ApiConnectionStatement statement, GuildPayload payload) {
    this.statement = Objects.requireNonNull(statement);
    this.id = payload.getId();
    this.rpgId = payload.getRpgId();
    this.fromPayload(payload);
  }

  private void fromPayload(GuildPayload payload) {
    this.rollCategoryId = payload.getRollCategoryId();
    this.offCategoryId = payload.getOffCategoryId();
  }

  public void setLoadedRpg(Rpg rpg) {
    Objects.requireNonNull(rpg);
    if (!rpg.getId().equals(this.getRpgId()))
      throw new IllegalArgumentException("ID of RPG does not match with guild registration");
    this.loadedRpg = rpg;
  }

  @Override
  public ObjectResolver<Attack> getAttackResolver() {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public ObjectResolver<Trainer> getTrainerResolver() {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public ObjectResolver<Art> getArtResolver() {
    throw new RuntimeException("Not yet implemented.");
  }

  @Override
  public Rpg getRpg() {
    return Objects.requireNonNull(this.loadedRpg);
  }

  @NotNull
  @Override
  public String getId() {
    return this.id;
  }

  @NotNull
  @Override
  public String getRpgId() {
    return this.rpgId;
  }

  @Override
  public String getRollCategoryId() {
    return this.rollCategoryId;
  }

  @Override
  public String getOffCategoryId() {
    return this.offCategoryId;
  }

  @Override
  public Guild delete() {
    statement.deleteGuild(this);
    return this;
  }
}
