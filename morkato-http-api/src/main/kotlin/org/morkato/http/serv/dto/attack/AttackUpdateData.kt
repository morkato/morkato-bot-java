package org.morkato.http.serv.dto.attack

import org.morkato.http.serv.dto.validation.NamePrefixArtSchema
import org.morkato.http.serv.dto.validation.DescriptionSchema
import org.morkato.http.serv.dto.validation.NameSchema
import org.morkato.http.serv.dto.validation.BannerSchema
import org.morkato.http.serv.dto.validation.AttrSchema
import java.math.BigDecimal

data class AttackUpdateData(
  @NameSchema val name: String?,
  @NamePrefixArtSchema val name_prefix_art: String?,
  @DescriptionSchema val description: String?,
  @BannerSchema val banner: String?,
  @AttrSchema val wisteria_turn: BigDecimal?,
  @AttrSchema val poison_turn: BigDecimal?,
  @AttrSchema val burn_turn: BigDecimal?,
  @AttrSchema val bleed_turn: BigDecimal?,
  @AttrSchema val wisteria: BigDecimal?,
  @AttrSchema val poison: BigDecimal?,
  @AttrSchema val burn: BigDecimal?,
  @AttrSchema val bleed: BigDecimal?,
  @AttrSchema val stun: BigDecimal?,
  @AttrSchema val damage: BigDecimal?,
  @AttrSchema val breath: BigDecimal?,
  @AttrSchema val blood: BigDecimal?,
  val flags: Int?
) {}
