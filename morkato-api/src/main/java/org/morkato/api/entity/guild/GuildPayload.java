package org.morkato.api.entity.guild;

import javax.annotation.Nonnull;

public interface GuildPayload extends GuildId {
  @Nonnull
  String getRpgId();
  String getRollCategoryId();
  String getOffCategoryId();
}
