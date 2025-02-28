package org.morkato.http.serv.dto.family

import org.morkato.http.serv.dto.validation.DescriptionSchema
import org.morkato.http.serv.dto.validation.BannerSchema
import org.morkato.http.serv.dto.validation.NameSchema
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class FamilyUpdateData(
  @NameSchema val name: String?,
  val percent: BigDecimal?,
  val user_type: Int?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?
) {}
