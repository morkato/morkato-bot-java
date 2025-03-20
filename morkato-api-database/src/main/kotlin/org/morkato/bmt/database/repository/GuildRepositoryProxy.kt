package org.morkato.bmt.database.repository

import org.morkato.bmt.api.entity.guild.Guild
import org.morkato.bmt.api.exception.guild.GuildNotFoundError
import org.morkato.bmt.api.repository.GuildRepository
import org.morkato.bmt.api.repository.queries.GuildCreateQuery
import org.morkato.bmt.api.repository.queries.GuildIdQuery

class GuildRepositoryProxy(
  private val repository: GuildRepository
) : GuildRepository {
  private val guilds: MutableMap<String, Guild> = mutableMapOf()
  override fun fetch(query: GuildIdQuery): Guild {
    val cached: Guild? = guilds.get(query.id())
    if (cached != null)
      return cached
    val guild: Guild = try {
      repository.fetch(query)
    } catch (exc: GuildNotFoundError) {
      val creation: GuildCreateQuery = GuildCreateQuery()
        .setId(query.id())
      repository.create(creation)
    }
    guilds.put(guild.getId(), guild)
    return guild
  }

  override fun fetch(id: String): Guild {
    return this.fetch(GuildIdQuery(id))
  }

  override fun create(query: GuildCreateQuery): Guild {
    throw RuntimeException("This is a invalid context.")
  }

  override fun delete(guild: Guild) {
    this.repository.delete(guild)
    guilds.remove(guild.getId())
  }
}