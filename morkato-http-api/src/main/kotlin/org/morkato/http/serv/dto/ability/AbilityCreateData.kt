package org.morkato.http.serv.dto.ability

import jakarta.validation.constraints.NotNull
import org.morkato.http.serv.dto.validation.DescriptionSchema
import org.morkato.http.serv.dto.validation.BannerSchema
import org.morkato.http.serv.dto.validation.NameSchema
import java.math.BigDecimal

data class AbilityCreateData(
  @NameSchema @NotNull val name: String,
  val percent: BigDecimal?,
  val user_type: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
) {}