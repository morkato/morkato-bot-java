package org.morkato.http.serv.exception.model

import org.morkato.http.serv.exception.GuildObjectNotFound
import org.morkato.http.serv.exception.ModelType

class ArtNotFoundError(
  guildId: String,
  id: String
) : GuildObjectNotFound(ModelType.ART, guildId, id) {
}