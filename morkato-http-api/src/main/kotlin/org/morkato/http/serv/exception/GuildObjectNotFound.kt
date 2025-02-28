package org.morkato.http.serv.exception

open class GuildObjectNotFound(
  modelType: ModelType,
  guildId: String,
  id: String
) : NotFoundError(modelType, mapOf(
  "guild_id" to guildId,
  "id" to id
)) {
}