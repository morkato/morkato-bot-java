package org.morkato.database.model

import org.jetbrains.exposed.sql.ResultRow
import org.morkato.api.entity.guild.Guild
import org.morkato.api.entity.values.GuildDefaultValue
import org.morkato.database.infra.tables.guilds
import java.math.BigDecimal

class DatabaseGuild(
  private val id: String,
  private val humanInitialLife: BigDecimal,
  private val oniInitialLife: BigDecimal,
  private val hybridInitialLife: BigDecimal,
  private val breathInitial: BigDecimal,
  private val bloodInitial: BigDecimal,
  private val abilityRoll: BigDecimal,
  private val familyRoll: BigDecimal
) : Guild {
  companion object {
    fun from(row: ResultRow) : DatabaseGuild {
      return DatabaseGuild(
        id = row[guilds.id],
        humanInitialLife = row[guilds.humanInitialLife],
        oniInitialLife = row[guilds.oniInitialLife],
        hybridInitialLife = row[guilds.hybridInitialLife],
        breathInitial = row[guilds.breathInitial],
        bloodInitial = row[guilds.bloodInitial],
        abilityRoll = row[guilds.abilityRoll],
        familyRoll = row[guilds.familyRoll],
      )
    }

    fun from(values: GuildDefaultValue, id: String): Guild {
      return DatabaseGuild(
        id = id,
        humanInitialLife = values.getHumanInitialLife(),
        oniInitialLife = values.getOniInitialLife(),
        hybridInitialLife = values.getHybridInitialLife(),
        breathInitial = values.getBreathInitial(),
        bloodInitial = values.getBloodInitial(),
        abilityRoll = values.getAbilityRoll(),
        familyRoll = values.getFamilyRoll()
      )
    }
  }
  override fun getId(): String {
    return this.id
  }

  override fun getHumanInitialLife(): BigDecimal {
    return this.humanInitialLife
  }

  override fun getOniInitialLife(): BigDecimal {
    return this.oniInitialLife
  }

  override fun getHybridInitialLife(): BigDecimal {
    return this.hybridInitialLife
  }

  override fun getBreathInitial(): BigDecimal {
    return this.breathInitial
  }

  override fun getBloodInitial(): BigDecimal {
    return this.bloodInitial
  }

  override fun getAbilityRoll(): BigDecimal {
    return this.abilityRoll
  }

  override fun getFamilyRoll(): BigDecimal {
    return this.familyRoll
  }
}