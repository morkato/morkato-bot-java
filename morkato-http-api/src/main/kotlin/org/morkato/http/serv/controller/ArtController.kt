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
import org.springframework.context.annotation.Profile
import jakarta.validation.Valid

import org.morkato.http.serv.exception.model.ArtNotFoundError
import org.morkato.http.serv.exception.model.GuildNotFoundError
import org.morkato.http.serv.dto.validation.IdSchema
import org.morkato.http.serv.dto.art.ArtAttackResponseData
import org.morkato.http.serv.dto.art.ArtResponseData
import org.morkato.http.serv.dto.art.ArtCreateData
import org.morkato.http.serv.dto.art.ArtUpdateData
import org.morkato.http.serv.infra.repository.GuildRepository
import org.morkato.http.serv.model.guild.Guild

@RestController
@RequestMapping("/arts/{guild_id}")
@Profile("api")
class ArtController {
  @GetMapping
  @Transactional
  fun findAllByGuildId(
    @PathVariable("guild_id") @IdSchema guild_id: String
  ) : List<ArtResponseData> {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val attacks = guild.getAllAttacks().toMutableList()
      guild.getAllArts()
        .map { art ->
          val (valid, invalid) = attacks.partition { art.id == it.artId }
          attacks.clear()
          attacks.addAll(invalid)
          ArtResponseData(art, valid.map(::ArtAttackResponseData))
        }.toList()
    } catch (exc: GuildNotFoundError) {
      listOf()
    }
  }
  @PostMapping
  @Transactional
  fun createArtByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @RequestBody @Valid data: ArtCreateData
  ) : ArtResponseData {
    val guild = Guild(GuildRepository.findOrCreate(guild_id))
    val art = guild.createArt(
      name = data.name,
      type = data.type,
      description = data.description,
      banner = data.banner,
      energy = data.energy,
      life = data.life,
      breath = data.breath,
      blood = data.blood
    )
    return ArtResponseData(art, listOf())
  }
  @GetMapping("/{id}")
  @Transactional
  fun getReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : ArtResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val art = guild.getArt(id.toLong())
      val attacks = art.getAllAttacks()
      ArtResponseData(art, attacks.map(::ArtAttackResponseData).toList())
    } catch (exc: GuildNotFoundError) {
      throw ArtNotFoundError(guild_id, id)
    }
  }
  @PutMapping("/{id}")
  @Transactional
  fun updateArtByGuild(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String,
    @RequestBody @Valid data: ArtUpdateData
  ) : ArtResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val before = guild.getArt(id.toLong())
      val art = before.update(
        name = data.name,
        type = data.type,
        description = data.description,
        banner = data.banner,
        energy = data.energy,
        life = data.life,
        breath = data.breath,
        blood = data.blood
      )
      val attacks = art.getAllAttacks()
      ArtResponseData(art, attacks.map(::ArtAttackResponseData).toList())
    } catch (exc: GuildNotFoundError) {
      throw ArtNotFoundError(guild_id, id)
    }
  }
  @DeleteMapping("/{id}")
  @Transactional
  fun deleteArtByReference(
    @PathVariable("guild_id") @IdSchema guild_id: String,
    @PathVariable("id") @IdSchema id: String
  ) : ArtResponseData {
    return try {
      val guild = Guild(GuildRepository.findById(guild_id))
      val art = guild.getArt(id.toLong())
      val attacks = art.getAllAttacks()
      ArtResponseData(art.delete(), attacks.map(::ArtAttackResponseData).toList())
    } catch (exc: GuildNotFoundError) {
      throw ArtNotFoundError(guild_id, id)
    }
  }
}
