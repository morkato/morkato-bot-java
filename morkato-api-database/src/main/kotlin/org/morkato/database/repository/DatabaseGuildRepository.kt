package org.morkato.database.repository

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.morkato.api.entity.guild.Guild
import org.morkato.api.entity.values.GuildDefaultValue
import org.morkato.api.exception.guild.GuildIdInvalidError
import org.morkato.api.exception.guild.GuildNotFoundError
import org.morkato.api.exception.repository.RepositoryInternalError
import org.morkato.api.repository.GuildRepository
import org.morkato.api.repository.queries.GuildCreateQuery
import org.morkato.api.repository.queries.GuildIdQuery
import org.morkato.database.infra.tables.guilds
import org.morkato.database.model.DatabaseGuild
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class DatabaseGuildRepository(
  private val executor: ExecutorService,
  private val database: Database,
  private val validator: Validator
) : GuildRepository {
  override fun fetch(query: GuildIdQuery): Future<Guild> {
      return@fetch CompletableFuture.supplyAsync({
        try {
          query.validate(validator)
          val row = transaction(database) {
            return@transaction guilds.select { guilds.id eq query.id() }
              .limit(1)
              .single()
          }
          return@supplyAsync DatabaseGuild.from(row);
        } catch (exc: NoSuchElementException) {
          throw GuildNotFoundError(query.id())
        } catch (exc: ConstraintViolationException) {
          throw GuildIdInvalidError(query.id())
        } catch (exc: Throwable) {
          throw RepositoryInternalError(exc);
        }
      }, executor);
  }

  override fun fetch(id: String): Future<Guild> {
    return this.fetch(GuildIdQuery(id));
  }

  override fun create(query: GuildCreateQuery): Future<Guild> {
    return@create CompletableFuture.supplyAsync({
      try {
        query.validate(validator)
        val row = transaction(database) {
          guilds.insert {
            it[this.id] = query.getId()
            if (query.getHumanInitialLife() != null)
              it[this.humanInitialLife] = query.getHumanInitialLife()
            if (query.getOniInitialLife() != null)
              it[this.oniInitialLife] = query.getOniInitialLife()
            if (query.getHybridInitialLife() != null)
              it[this.hybridInitialLife] = query.getHybridInitialLife()
            if (query.getBreathInitial() != null)
              it[this.breathInitial] = query.getBreathInitial()
            if (query.getBloodInitial() != null)
              it[this.bloodInitial] = query.getBloodInitial()
            if (query.getAbilityRoll() != null)
              it[this.abilityRoll] = query.getAbilityRoll()
            if (query.getFamilyRoll() != null)
              it[this.familyRoll] = query.getFamilyRoll()
          }
        }
        val id = row get guilds.id
        return@supplyAsync DatabaseGuild.from(GuildDefaultValue.from(query), id)
      } catch (exc: Throwable) {
        throw RepositoryInternalError(exc)
      }
    }, executor)
  }

  override fun delete(guild: Guild) : Future<Void> {
    return@delete CompletableFuture.supplyAsync({
      try {
        transaction(database) {
          guilds.deleteWhere {
            this.id eq guild.getId()
          }
        }
        return@supplyAsync null
      } catch (exc: Throwable) {
        throw RepositoryInternalError(exc)
      }
    }, executor)
  }
}