package org.morkato.database

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.morkato.utility.MorkatoConfigLoader
import org.jetbrains.exposed.sql.Database
import org.morkato.api.entity.guild.Guild
import org.morkato.api.repository.GuildRepository
import org.morkato.database.repository.DatabaseGuildRepository
import java.util.Properties
import java.util.concurrent.Executors

fun main(args: Array<String>) {
  val executor = Executors.newFixedThreadPool(10)
  try {
    val settings: Properties = MorkatoConfigLoader.loadDefault()
    val database: Database = DatabaseProvider.get(settings).getDatabase()
    val validator: Validator = Validation.buildDefaultValidatorFactory().getValidator()
    val repository: GuildRepository = DatabaseGuildRepository(executor, database, validator)
    val guild: Guild = repository.fetch("11111").get()
    println(guild)
  } catch (exception: Throwable) {
    println(exception)
    executor.shutdown();
  }
}