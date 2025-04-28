package org.morkato.api.entity.rpg;

import org.morkato.api.entity.DeleteApiModel;
import org.morkato.api.entity.UpdateApiModel;

public interface Rpg
  extends UpdateApiModel<Rpg, RpgUpdateBuilder>,
          DeleteApiModel<Rpg>,
          RpgId,
          RpgPayload {
}
