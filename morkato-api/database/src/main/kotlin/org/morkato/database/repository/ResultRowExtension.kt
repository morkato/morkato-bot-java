package org.morkato.database.repository

import org.jetbrains.exposed.sql.ResultRow
import org.morkato.api.dto.ArtDTO
import org.morkato.api.dto.GuildDTO
import org.morkato.database.infra.tables.arts
import org.morkato.database.infra.tables.guilds

fun ResultRow.getGuildDTO(): GuildDTO {
  return GuildDTO()
    .setId(this[guilds.id])
    .setHumanInitialLife(this[guilds.humanInitialLife])
    .setOniInitialLife(this[guilds.oniInitialLife])
    .setHybridInitialLife(this[guilds.hybridInitialLife])
    .setBreathInitial(this[guilds.breathInitial])
    .setBloodInitial(this[guilds.bloodInitial])
    .setAbilityRoll(this[guilds.abilityRoll])
    .setFamilyRoll(this[guilds.familyRoll])
}

fun ResultRow.getArtDTO(): ArtDTO {
  return ArtDTO()
    .setGuildId(this[arts.guild_id])
    .setId(this[arts.id].toString())
    .setName(this[arts.name])
    .setType(this[arts.type])
    .setDescription(this[arts.description])
    .setBanner(this[arts.banner])
}