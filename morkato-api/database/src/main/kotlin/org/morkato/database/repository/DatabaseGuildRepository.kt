package org.morkato.database.repository

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.morkato.api.dto.GuildDTO
import org.morkato.api.entity.guild.Guild
import org.morkato.api.entity.guild.GuildId
import org.morkato.api.entity.guild.GuildPayload
import org.morkato.api.exception.guild.GuildAlreadyExistsError
import org.morkato.api.values.GuildDefaultValue
import org.morkato.api.exception.guild.GuildIdInvalidError
import org.morkato.api.exception.guild.GuildNotFoundError
import org.morkato.api.exception.repository.RepositoryInternalError
import org.morkato.api.repository.guild.GuildRepository
import org.morkato.api.repository.guild.GuildCreationQuery
import org.morkato.api.repository.guild.GuildIdQuery
import org.morkato.database.PSQLViolations
import org.morkato.database.infra.tables.guilds
import org.postgresql.util.PSQLException

class DatabaseGuildRepository(
  private val database: Database,
  private val validator: Validator
) : GuildRepository {
  private fun fetchRequest(query: GuildId): GuildDTO {
    try {
      GuildDTO.from(query).validateForId(validator)
      val builder = guilds.select { guilds.byId(query) }
        .limit(1)
      val row = builder.single()
      return row.getGuildDTO()
    } catch (exc: NoSuchElementException) {
      throw GuildNotFoundError(query.getId())
    } catch (exc: ConstraintViolationException) {
      throw  GuildIdInvalidError(query.getId())
    } catch (exc: Throwable) {
      throw RepositoryInternalError(exc)
    }
  }
  
  private fun createRequest(query: GuildCreationQuery): GuildDTO {
    try {
      query.validate(validator)
      val statement = guilds.insert {
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
      return GuildDefaultValue.from(query).getDTO()
        .setId(query.getId())
    } catch (exc: ExposedSQLException) {
      val cause = exc.cause as? PSQLException
      if (PSQLViolations.isDuplicateKeyViolation(cause, "guild.pkey"))
        throw GuildAlreadyExistsError(query)
      throw RepositoryInternalError(exc)
    } catch (exc: Throwable) {
      throw RepositoryInternalError(exc)
    }
  }

  private fun deleteRequest(guild: GuildId) {
    try {
      guilds.deleteWhere { this.byId(guild) }
    } catch (exc: Throwable) {
      throw RepositoryInternalError(exc)
    }
  }

  override fun fetch(query: GuildId): GuildPayload = transaction(database) { GuildPayload.create(fetchRequest(query)) }
  override fun fetch(id: String): GuildPayload = fetch(GuildIdQuery(id))
  override fun create(query: GuildCreationQuery): GuildPayload = transaction(database) { GuildPayload.create(createRequest(query)) }
  override fun delete(guild: GuildId) = transaction(database) { deleteRequest(guild) }
}