package org.morkato.database.repository

import org.morkato.api.dto.GuildDTO
import org.morkato.api.entity.guild.Guild
import org.morkato.api.entity.guild.GuildId
import org.morkato.api.exception.guild.GuildNotFoundError
import org.morkato.api.repository.GuildRepository
import org.morkato.api.repository.queries.guild.GuildCreationQuery
import org.morkato.api.repository.queries.guild.GuildIdQuery

class GuildRepositoryProxy(
  private val repository: GuildRepository
) : GuildRepository {
  private val guilds: MutableMap<String, GuildDTO> = mutableMapOf()
  override fun fetch(query: GuildId): GuildDTO {
    val cached: GuildDTO? = guilds.get(query.getId())
    if (cached != null)
      return cached
    val guild: GuildDTO = try {
      repository.fetch(query)
    } catch (exc: GuildNotFoundError) {
      val creation: GuildCreationQuery = GuildCreationQuery()
        .setId(query.getId())
      repository.create(creation)
    }
    guilds.put(guild.getId(), guild)
    return guild
  }

  override fun fetch(id: String): GuildDTO {
    return this.fetch(GuildIdQuery(id))
  }

  override fun create(query: GuildCreationQuery): GuildDTO {
    throw RuntimeException("This is a invalid context.")
  }

  override fun delete(guild: GuildId) {
    this.repository.delete(guild)
    guilds.remove(guild.getId())
  }
}