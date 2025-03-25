package org.morkato.api.entity.ability;

import org.morkato.api.entity.ApiObject;
import org.morkato.api.entity.DeleteApiModel;
import org.morkato.api.entity.UpdateApiModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Ability
  extends DeleteApiModel<Ability>,
          UpdateApiModel<Ability, AbilityUpdateBuilder>,
          ApiObject,
          AbilityId {
  @Nonnull
  String getPercent();
  int getUserType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
