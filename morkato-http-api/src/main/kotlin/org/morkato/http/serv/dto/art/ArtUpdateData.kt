package org.morkato.http.serv.dto.art

import jakarta.validation.constraints.Digits
import org.morkato.http.serv.dto.validation.DescriptionSchema
import org.morkato.http.serv.dto.validation.BannerSchema
import org.morkato.http.serv.dto.validation.NameSchema
import org.morkato.http.serv.dto.validation.AttrSchema
import org.morkato.http.serv.model.art.ArtType
import java.math.BigDecimal

data class ArtUpdateData(
  @NameSchema val name: String?,
  val type: ArtType?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  @Digits(integer = 3, fraction = 0)
  val energy: BigDecimal?,
  @AttrSchema val life: BigDecimal?,
  @AttrSchema val breath: BigDecimal?,
  @AttrSchema val blood: BigDecimal?
) {}
