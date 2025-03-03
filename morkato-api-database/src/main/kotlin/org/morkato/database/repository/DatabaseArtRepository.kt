package org.morkato.database.repository

import jakarta.validation.Validator
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.morkato.api.dto.ArtDTO
import org.morkato.api.entity.art.Art
import org.morkato.api.entity.art.ArtId
import org.morkato.api.entity.impl.art.ApiArtImpl
import org.morkato.api.repository.ArtRepository
import org.morkato.api.repository.SimpleRepositoryCentral
import org.morkato.api.repository.queries.ArtCreationQuery
import org.morkato.api.repository.queries.ArtUpdateQuery
import org.morkato.database.infra.tables.arts

class DatabaseArtRepository(
  private val central: SimpleRepositoryCentral,
  private val database: Database,
  private val validator: Validator
) : ArtRepository {
  private fun getDTOFromRow(row: ResultRow): ArtDTO {
    val dto: ArtDTO = ArtDTO()
      .setGuildId(row[arts.guild_id])
      .setId(row[arts.id].toString())
      .setName(row[arts.name])
      .setType(row[arts.type])
      .setDescription(row[arts.description])
      .setBanner(row[arts.banner])
    dto.validate(validator)
    return dto
  }

  override fun fetchAll(guildId: String): Array<ArtDTO> {
//    TODO("Not yet implemented ArtRepository::fetchAll in DatabaseArtRepository (Not proxied)")
    return transaction(database) {
      return@transaction arts.select { arts.guild_id eq guildId }
        .map(this@DatabaseArtRepository::getDTOFromRow)
        .toTypedArray()
    }
  }

  override fun fetch(query: ArtId): ArtDTO {
    TODO("Not yet implemented")
  }

  override fun create(query: ArtCreationQuery): ArtDTO {
    TODO("Not yet implemented")
  }

  override fun update(query: ArtUpdateQuery): ArtDTO {
    TODO("Not yet implemented")
  }

  override fun delete(art: ArtId) {
    TODO("Not yet implemented")
  }
}