package org.morkato.api.entity.user;

import org.morkato.api.entity.ObjectId;

public interface UserId extends ObjectId {
  String getGuildId();
}
