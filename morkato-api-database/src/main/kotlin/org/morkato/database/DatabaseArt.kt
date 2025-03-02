package org.morkato.database

import org.morkato.api.entity.art.Art
import org.morkato.api.entity.art.ArtUpdateBuilder
import org.morkato.api.entity.art.ArtType
import org.morkato.api.entity.guild.Guild
import java.util.concurrent.Future

class DatabaseArt() : Art {
  override fun getGuildId(): String {
    TODO("Not yet implemented")
  }

  override fun getGuild(): Guild {
    TODO("Not yet implemented")
  }

  override fun getId(): String {
    TODO("Not yet implemented")
  }

  override fun getName(): String {
    TODO("Not yet implemented")
  }

  override fun getType(): ArtType {
    TODO("Not yet implemented")
  }

  override fun getDescription(): String? {
    TODO("Not yet implemented")
  }

  override fun getBanner(): String? {
    TODO("Not yet implemented")
  }

  override fun doUpdate(): ArtUpdateBuilder {
    TODO("Not yet implemented")
  }

  override fun delete(): Future<Art> {
    TODO("Not yet implemented")
  }
}