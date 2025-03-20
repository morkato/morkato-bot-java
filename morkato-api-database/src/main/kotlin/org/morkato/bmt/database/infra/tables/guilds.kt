package org.morkato.bmt.database.infra.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.math.BigDecimal

object guilds : Table("guilds") {
  val id: Column<String> = discordSnowflakeIdType("id")

  val humanInitialLife: Column<BigDecimal> = attrType("human_initial_life")
  val oniInitialLife: Column<BigDecimal> = attrType("oni_initial_life")
  val hybridInitialLife: Column<BigDecimal> = attrType("hybrid_initial_life")
  val breathInitial: Column<BigDecimal> = attrType("breath_initial")
  val bloodInitial: Column<BigDecimal> = attrType("blood_initial")
  val familyRoll: Column<BigDecimal> = rollType("family_roll")
  val abilityRoll: Column<BigDecimal> = rollType("ability_roll")

  override val primaryKey: PrimaryKey = PrimaryKey(id)
}