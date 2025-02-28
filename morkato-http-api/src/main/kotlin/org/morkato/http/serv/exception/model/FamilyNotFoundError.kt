package org.morkato.http.serv.exception.model

import org.morkato.http.serv.exception.GuildObjectNotFound
import org.morkato.http.serv.exception.ModelType

class FamilyNotFoundError(
  guildId: String,
  id: String
) : GuildObjectNotFound(ModelType.FAMILY, guildId, id) {
}