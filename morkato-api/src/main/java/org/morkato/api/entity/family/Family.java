package org.morkato.api.entity.family;

import org.morkato.api.entity.EntityNamed;
import org.morkato.api.entity.DeleteApiModel;
import org.morkato.api.entity.UpdateApiModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;

public interface Family
  extends DeleteApiModel<Family>,
          UpdateApiModel<Family, FamilyUpdateBuilder>,
  EntityNamed,
          FamilyId {
  @Nonnull
  BigDecimal getPercent();
  int getUserType();
  @Nullable
  String getDescription();
  @Nullable
  String getBanner();
}
