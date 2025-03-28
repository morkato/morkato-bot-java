package org.morkato.database.repository

import org.morkato.api.exception.art.ArtNotFoundException
import org.morkato.api.repository.art.ArtCreationQuery
import org.morkato.api.repository.art.ArtUpdateQuery
import org.morkato.api.repository.art.ArtRepository
import org.morkato.api.entity.art.ArtId
import org.morkato.api.dto.ArtDTO
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.morkato.database.infra.tables.arts
import jakarta.validation.Validator
import java.util.Objects

class DatabaseArtRepository(
  private val database: Database,
  private val validator: Validator
) : ArtRepository {
  private fun fetchAllRequest(guildId: String): Array<ArtDTO> {
    val builder = arts.select { arts.byGuildId(guildId) }
    val rows = builder.map(ResultRow::getArtDTO)
    return rows.toTypedArray()
  }

  private fun fetchRequest(query: ArtId): ArtDTO {
    try {
      val builder = arts.select { arts.byId(query) }
        .limit(1)
      val row = builder.single()
      return row.getArtDTO()
    } catch (exc: NoSuchElementException) {
      throw ArtNotFoundException(query)
    }
  }

  private fun createRequest(query: ArtCreationQuery): ArtDTO {
    query.validate(validator)
    val statement = arts.insert {
      it[this.guild_id] = query.guildId()
      it[this.name] = query.name()
      it[this.type] = query.type()
      if (Objects.nonNull(query.description()))
        it[this.description] = query.description()
      if (Objects.nonNull(query.banner()))
        it[this.banner] = query.banner()
    }
    return ArtDTO.from(query)
      .setId((statement get arts.id).toString())
  }

  private fun updateRequest(query: ArtUpdateQuery) {
    query.validate(validator)
    arts.update({ arts.byId(query) }) {
      if (Objects.nonNull(query.name()))
        it[this.name] = query.name()
      if (Objects.nonNull(query.type()))
        it[this.type] = query.type()
      if (Objects.nonNull(query.description()))
        it[this.description] = query.description()
      if (Objects.nonNull(query.banner()))
        it[this.banner] = query.banner()
    }
  }

  private fun deleteRequest(query: ArtId) {
    arts.deleteWhere(1) { this.byId(query) }
  }

  override fun fetchAll(guildId: String): Array<ArtDTO> = transaction(database) { fetchAllRequest(guildId) }
  override fun fetch(query: ArtId): ArtDTO = transaction(database) { fetchRequest(query) }
  override fun create(query: ArtCreationQuery): ArtDTO = transaction(database) { createRequest(query) }
  override fun update(query: ArtUpdateQuery) = transaction(database) { updateRequest(query) }
  override fun delete(art: ArtId) = transaction(database) { deleteRequest(art) }
}