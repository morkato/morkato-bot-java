package org.morkato.http.serv.infra.tables

import org.morkato.http.serv.model.image.ImageType
import org.jetbrains.exposed.sql.Table

object images : Table("images") {
  val author_id = discordSnowflakeIdType("author_id")
  val name = nameType("name")
  val type = enumerationByName<ImageType>("type", length = 16)
  val file = text("file")

  override val primaryKey = PrimaryKey(author_id, name)
}