package org.morkato.http.serv.controller

import org.morkato.http.serv.dto.guild.GuildResponseData
import org.morkato.http.serv.model.guild.Guild
import org.morkato.http.serv.dto.validation.IdSchema
import org.morkato.http.serv.exception.model.GuildNotFoundError
import org.morkato.http.serv.infra.repository.GuildRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/guilds/{id}")
@Profile("api")
class GuildController {
  @GetMapping
  @Transactional
  fun getGuildByRef(
    @PathVariable("id") @IdSchema id: String
  ) : GuildResponseData {
    val payload = try {GuildRepository.findById(id)} catch (exc: GuildNotFoundError) {GuildRepository.virtual(id)}
    val guild = Guild(payload)
    return GuildResponseData(guild)
  }
}