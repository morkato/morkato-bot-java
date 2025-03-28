package org.morkato.database

import org.morkato.database.infra.tables.arts
import org.morkato.api.entity.art.ArtId
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.Op

object OperationQueries {
  fun buildFetchUniqueArtQuery(query: ArtId): Op<Boolean> {
    return Op.build {
      arts.guild_id.eq(query.getGuildId())
        .and(arts.id.eq(query.getId().toLong()))
    }
  }
}