package org.morkato.api.entity.user;

import org.morkato.api.entity.ApiObject;
import org.morkato.api.entity.DeleteApiModel;
import org.morkato.api.entity.UpdateApiModel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface User
  extends DeleteApiModel<User>,
          UpdateApiModel<User, UserUpdateBuilder>,
          ApiObject,
          UserId {
  @Nonnull
  UserType getType();
  int getFlags();
  @Nonnull
  BigDecimal getAbilityRoll();
  @Nonnull
  BigDecimal getFamilyRoll();
  @Nonnull
  BigDecimal getProdigyRoll();
  @Nonnull
  BigDecimal getMarkRoll();
  @Nonnull
  BigDecimal getBerserkRoll();
}
