package org.morkato.database.infra.tables

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.morkato.api.entity.art.ArtId

object arts : Table("arts") {
  val guild_id = discordSnowflakeIdType("guild_id")
  val id = idType("id").autoIncrement()

  val name = nameType("name")
  val type = artType("type")
  val description = descriptionType("description").nullable()
  val banner = bannerType("banner").nullable()

  override val primaryKey = PrimaryKey(guild_id, id)

  fun byGuildId(guildId: String): Op<Boolean> {
    val expression: Op<Boolean> = this.guild_id.eq(guildId)
    return Op.build { expression }
  }

  fun byId(query: ArtId): Op<Boolean> {
    val expression: Op<Boolean> = (
      (this.guild_id.eq(query.getGuildId()))
        .and(this.id.eq(query.getId().toLong()))
    )
    return Op.build { expression }
  }
}