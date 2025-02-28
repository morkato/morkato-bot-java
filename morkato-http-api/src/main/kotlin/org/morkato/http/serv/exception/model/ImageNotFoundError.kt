package org.morkato.http.serv.exception.model

import org.morkato.http.serv.exception.ModelType
import org.morkato.http.serv.exception.NotFoundError

class ImageNotFoundError(extra: Map<String, Any?>) : NotFoundError(ModelType.IMAGE, extra) {
}