package org.morkato.http.serv.dto.user

import org.morkato.http.serv.model.user.UserType
import org.morkato.http.serv.model.user.User
import java.math.BigDecimal

data class UserResponseData(
  val guild_id: String,
  val id: String,
  val type: UserType,
  val flags: Int?,
  val ability_roll: BigDecimal?,
  val family_roll: BigDecimal?,
  val prodigy_roll: BigDecimal?,
  val mark_roll: BigDecimal?,
  val berserk_roll: BigDecimal?,
  val abilities: List<String>,
  val families: List<String>
) {
  public constructor(user: User) : this(
    user.guild.id,
    user.id,
    user.type,
    user.flags,
    user.abilityRoll,
    user.familyRoll,
    user.prodigyRoll,
    user.markRoll,
    user.berserkRoll,
    user.getAllAbilities().map(Long::toString).toList(),
    user.getAllFamilies().map(Long::toString).toList()
  )
}