package org.morkato.http.serv.controller

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping

import org.morkato.http.serv.exception.model.AttackNotFoundError
import org.morkato.http.serv.exception.model.GuildNotFoundError
import org.morkato.http.serv.exception.model.ArtNotFoundError
import org.morkato.http.serv.infra.repository.GuildRepository
import org.morkato.http.serv.model.guild.Guild
import org.morkato.http.serv.dto.attack.AttackResponseData
import org.morkato.http.serv.dto.attack.AttackCreateData
import org.morkato.http.serv.dto.attack.AttackUpdateData
import org.morkato.http.serv.dto.validation.IdSchema
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile

@RestController
@RequestMapping("/attacks/{guild_id}")
@Profile("api")
class AttackController {
  @GetMapping
  @Transactional
  fun getAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<AttackResponseData> {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      guild.getAllAttacks()
        .map(::AttackResponseData)
        .toList()
    } catch (exc: GuildNotFoundError) {
      listOf()
    }
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val attack = guild.getAttack(id.toLong())
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      throw AttackNotFoundError(guild_id, id)
    }
  }
  @PostMapping("/{art_id}")
  @Transactional
  fun createAttackByArt(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("art_id") @IdSchema art_id: String,
    @RequestBody @Valid data: AttackCreateData
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val art = guild.getArt(art_id.toLong())
      val attack = art.createAttack(
        name = data.name,
        namePrefixArt = data.name_prefix_art,
        description = data.description,
        banner = data.banner,
        wisteriaTurn = data.wisteria_turn,
        poisonTurn = data.poison_turn,
        burnTurn = data.burn_turn,
        bleedTurn = data.bleed_turn,
        wisteria = data.wisteria,
        poison = data.poison,
        burn = data.burn,
        bleed = data.bleed,
        stun = data.stun,
        damage = data.damage,
        breath = data.breath,
        blood = data.blood,
        flags = data.flags
      )
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      throw ArtNotFoundError(guild_id, art_id)
    }
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateAttackById(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: AttackUpdateData
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val before = guild.getAttack(id.toLong())
      val attack = before.update(
        name = data.name,
        namePrefixArt = data.name_prefix_art,
        description = data.description,
        banner = data.banner,
        wisteriaTurn = data.wisteria_turn,
        poisonTurn = data.poison_turn,
        burnTurn = data.burn_turn,
        bleedTurn = data.bleed_turn,
        wisteria = data.wisteria,
        poison = data.poison,
        burn = data.burn,
        bleed = data.bleed,
        stun = data.stun,
        damage = data.damage,
        breath = data.breath,
        blood = data.blood,
        flags = data.flags
      )
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      throw AttackNotFoundError(guild_id, id)
    }
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : AttackResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val attack = guild.getAttack(id.toLong())
      attack.delete()
      AttackResponseData(attack)
    } catch (exc: GuildNotFoundError) {
      throw AttackNotFoundError(guild_id, id)
    }
  }
}
