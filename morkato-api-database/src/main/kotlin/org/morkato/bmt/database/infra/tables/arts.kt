package org.morkato.bmt.database.infra.tables

import org.jetbrains.exposed.sql.Table

object arts : Table("arts") {
  val guild_id = discordSnowflakeIdType("guild_id")
  val id = idType("id").autoIncrement()

  val name = nameType("name")
  val type = artType("type")
  val description = descriptionType("description").nullable()
  val banner = bannerType("banner").nullable()

  override val primaryKey = PrimaryKey(guild_id, id)
}