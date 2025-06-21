package org.morkato.database;

import org.morkato.api.ApiConnectionStatement;
import org.morkato.api.dto.RpgDTO;
import org.morkato.api.entity.guild.Guild;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.entity.guild.GuildPayload;
import org.morkato.api.entity.rpg.Rpg;
import org.morkato.api.entity.rpg.RpgId;
import org.morkato.api.entity.rpg.RpgPayload;
import org.morkato.api.exception.guild.GuildNotFoundException;
import org.morkato.api.exception.repository.RepositoryException;
import org.morkato.api.internal.entity.guild.GuildEntity;
import org.morkato.api.internal.entity.rpg.RpgEntity;
import org.morkato.api.repository.guild.GuildCreationQuery;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.api.repository.rpg.RpgCreationQuery;
import org.morkato.api.repository.rpg.RpgRepository;
import org.morkato.database.repository.guild.PsqlGuildRepository;
import org.morkato.database.repository.rpg.PsqlRpgRepository;
import org.morkato.jdbc.ReflectionQueryLoader;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PsqlConnectionStatement implements ApiConnectionStatement {
  private final PsqlRepositoryStatement repository;
  private final GuildRepository guildRepository;
  private final RpgRepository rpgRepository;
  private final Map<String, Guild> guilds = new HashMap<>();
  private final Map<String, Rpg> rpgs = new HashMap<>();

  public PsqlConnectionStatement(PsqlRepositoryStatement repository) {
    this.repository = Objects.requireNonNull(repository);
    this.guildRepository = new PsqlGuildRepository(repository);
    this.rpgRepository = new PsqlRpgRepository(repository);
  }

  public void prepare(ReflectionQueryLoader loader) {
    loader.writeAll(guildRepository);
    loader.writeAll(rpgRepository);
  }

  @Override
  public Guild fetchGuild(GuildId query) throws RepositoryException {
    final GuildPayload payload = guildRepository.fetch(query);
    final Rpg rpg = this.fetchRpg(new RpgDTO().setId(payload.getRpgId()));
    final GuildEntity guild = new GuildEntity(this, payload);
    guild.setLoadedRpg(rpg);
    guilds.put(guild.getId(), guild);
    return guild;
  }

  @Override
  public Guild createGuild(GuildId query) throws RepositoryException {
    final DefaultTransactionDefinition def = repository.newTransactionDefinition();
    final TransactionStatus status = repository.getTransactionStatus(def);
    try {
      final RpgPayload rpgpayload = this.rpgRepository.create();
      final RpgEntity rpg = new RpgEntity(this, rpgpayload);
      final GuildCreationQuery creationQuery = new GuildCreationQuery()
        .setId(query.getId())
        .setRpgId(rpg.getId());
      final GuildPayload payload = guildRepository.create(creationQuery);
      final GuildEntity guild = new GuildEntity(this, payload);
      guild.setLoadedRpg(rpg);
      repository.commit(status);
      guilds.put(guild.getId(), guild);
      rpgs.put(rpg.getId(), rpg);
      return guild;
    } catch (Throwable exc) {
      repository.rollback(status);
      throw exc;
    }
  }

  @Override
  public Guild fetchOrCreateGuild(GuildId query) throws RepositoryException {
    try {
      return this.fetchGuild(query);
    } catch (GuildNotFoundException exc) {
      return this.createGuild(query);
    }
  }

  @Override
  public void deleteGuild(GuildId query) throws RepositoryException {
    this.guildRepository.delete(query);
    this.guilds.remove(query.getId());
  }

  @Override
  public Rpg fetchRpg(RpgId query) throws RepositoryException {
    final RpgPayload payload = rpgRepository.find(query);
    final RpgEntity rpg = new RpgEntity(this, payload);
    this.rpgs.put(rpg.getId(), rpg);
    return rpg;
  }

  @Override
  public Rpg createRpg(RpgCreationQuery query) throws RepositoryException {
    final DefaultTransactionDefinition def = repository.newTransactionDefinition();
    final TransactionStatus status = repository.getTransactionStatus(def);
    try {
      final RpgPayload payload = rpgRepository.create(query);
      final RpgEntity rpg = new RpgEntity(this, payload);
      repository.commit(status);
      rpgs.put(rpg.getId(), rpg);
      return rpg;
    } catch (Throwable exc) {
      repository.rollback(status);
      throw exc;
    }
  }

  @Override
  public void deleteRpg(RpgId query) throws RepositoryException {
    final DefaultTransactionDefinition def = repository.newTransactionDefinition();
    final TransactionStatus status = repository.getTransactionStatus(def);
    try {
      rpgRepository.delete(query);
      rpgs.remove(query.getId());
      repository.commit(status);
    } catch (Throwable exc) {
      repository.rollback(status);
      throw exc;
    }
  }
}
