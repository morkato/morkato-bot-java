package org.morkato.http.serv.controller

import org.morkato.http.serv.dto.user.UserCreateData
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.context.annotation.Profile
import org.morkato.http.serv.dto.user.UserResponseData
import org.morkato.http.serv.dto.user.UserUpdateData
import org.morkato.http.serv.dto.validation.IdSchema
import org.morkato.http.serv.exception.model.GuildNotFoundError
import org.morkato.http.serv.exception.model.UserNotFoundError
import org.morkato.http.serv.infra.repository.GuildRepository
import org.morkato.http.serv.model.guild.Guild

@RestController
@RequestMapping("/users/{guild_id}/{id}")
@Profile("api")
class UserController {
  @GetMapping
  @Transactional
  fun getUserById(
    @PathVariable("guild_id") @IdSchema guildId: String,
    @PathVariable("id") @IdSchema id: String
  ) : UserResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guildId))
      val user = guild.getUser(id)
      UserResponseData(user)
    } catch (exc: GuildNotFoundError) {
      throw UserNotFoundError(guildId, id)
    }
  }
  @PostMapping
  @Transactional
  fun createUser(
    @PathVariable("guild_id") @IdSchema guildId: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody data: UserCreateData
  ) : UserResponseData {
    val guild = Guild(GuildRepository.findOrCreate(guildId))
    val user = guild.createUser(
      id = id,
      type = data.type,
      flags = data.flags,
      abilityRoll = data.ability_roll,
      familyRoll = data.family_roll,
      prodigyRoll = data.prodigy_roll,
      markRoll = data.mark_roll,
      berserkRoll = data.berserk_roll
    )
    return UserResponseData(user)
  }
  @PutMapping
  @Transactional
  fun updateUser(
    @PathVariable("guild_id") @IdSchema guildId: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody data: UserUpdateData
  ) : UserResponseData {
    return try {
      val guild = Guild(GuildRepository.findOrCreate(guildId))
      val before = guild.getUser(id)
      val user = before.update(
        flags = data.flags,
        abilityRoll = data.ability_roll,
        familyRoll = data.family_roll,
        prodigyRoll = data.prodigy_roll,
        markRoll = data.mark_roll,
        berserkRoll = data.berserk_roll
      )
      UserResponseData(user)
    } catch (exc: GuildNotFoundError) {
      throw UserNotFoundError(guildId, id)
    }
  }
  @DeleteMapping
  @Transactional
  fun delUser(
    @PathVariable("guild_id") @IdSchema guildId: String,
    @PathVariable("id") @IdSchema id: String
  ) : UserResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guildId))
      val user = guild.getUser(id)
      user.delete()
      UserResponseData(user)
    } catch (exc: GuildNotFoundError) {
      throw UserNotFoundError(guildId, id)
    }
  }
  @PostMapping("/abilities/{ability_id}")
  @Transactional
  fun createUserAbility(
    @PathVariable("guild_id") @IdSchema guildId: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("ability_id") @IdSchema abilityId: String
  ) : UserResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guildId))
      val user = guild.getUser(id)
      user.syncAbility(abilityId.toLong())
      UserResponseData(user)
    } catch (exc: GuildNotFoundError) {
      throw UserNotFoundError(guildId, id)
    }
  }
  @PostMapping("/families/{family_id}")
  @Transactional
  fun createUserFamily(
    @PathVariable("guild_id") @IdSchema guildId: String,
    @PathVariable("id") @IdSchema id: String,
    @PathVariable("family_id") @IdSchema familyId: String
  ) : UserResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guildId))
      val user = guild.getUser(id)
      user.syncFamily(familyId.toLong())
      UserResponseData(user)
    } catch (exc: GuildNotFoundError) {
      throw UserNotFoundError(guildId, id)
    }
  }
}