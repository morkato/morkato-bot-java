package org.morkato.database.repository

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.morkato.api.dto.GuildDTO
import org.morkato.api.entity.guild.Guild
import org.morkato.api.entity.guild.GuildId
import org.morkato.api.entity.impl.guild.ApiGuildImpl
import org.morkato.api.entity.values.GuildDefaultValue
import org.morkato.api.exception.guild.GuildIdInvalidError
import org.morkato.api.exception.guild.GuildNotFoundError
import org.morkato.api.exception.repository.RepositoryInternalError
import org.morkato.api.repository.GuildRepository
import org.morkato.api.repository.RepositoryCentral
import org.morkato.api.repository.queries.guild.GuildCreationQuery
import org.morkato.api.repository.queries.guild.GuildIdQuery
import org.morkato.database.infra.tables.guilds

class DatabaseGuildRepository(
  private val database: Database,
  private val validator: Validator
) : GuildRepository {
  private fun getDTOFromRow(row: ResultRow): GuildDTO {
    val dto = GuildDTO()
      .setId(row[guilds.id])
      .setHumanInitialLife(row[guilds.humanInitialLife])
      .setOniInitialLife(row[guilds.oniInitialLife])
      .setHybridInitialLife(row[guilds.hybridInitialLife])
      .setBreathInitial(row[guilds.breathInitial])
      .setBloodInitial(row[guilds.bloodInitial])
      .setAbilityRoll(row[guilds.abilityRoll])
      .setFamilyRoll(row[guilds.familyRoll])
    dto.validate(validator)
    return dto
  }

  override fun fetch(query: GuildId): GuildDTO {
    try {
      GuildDTO.from(query).validateForId(validator)
      val row = transaction(database) {
        return@transaction guilds.select { guilds.id eq query.getId() }
          .limit(1)
          .single()
      }
      return this.getDTOFromRow(row)
    } catch (exc: NoSuchElementException) {
      throw GuildNotFoundError(query.getId())
    } catch (exc: ConstraintViolationException) {
      throw GuildIdInvalidError(query.getId())
    } catch (exc: Throwable) {
      throw RepositoryInternalError(exc)
    }
  }

  override fun fetch(id: String): GuildDTO {
    return this.fetch(GuildIdQuery().setId(id))
  }

  override fun create(query: GuildCreationQuery): GuildDTO {
    try {
      query.validate(validator)
      val row = transaction(database) {
        return@transaction guilds.insert {
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
      return GuildDefaultValue.from(query).getDTO()
        .setId(id)
    } catch (exc: Throwable) {
      throw RepositoryInternalError(exc)
    }
  }

  override fun delete(guild: GuildId): Unit {
    try {
      transaction(database) {
        guilds.deleteWhere {
          this.id eq guild.getId()
        }
      }
    } catch (exc: Throwable) {
      throw RepositoryInternalError(exc)
    }
  }
}