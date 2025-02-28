package org.morkato.http.serv.exception.model

import org.morkato.http.serv.exception.ModelType
import org.morkato.http.serv.exception.NotFoundError

class GuildNotFoundError(extra: Map<String, Any?>) : NotFoundError(ModelType.GUILD, extra) {
}