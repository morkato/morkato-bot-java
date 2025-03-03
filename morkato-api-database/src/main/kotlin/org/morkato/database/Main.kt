package org.morkato.database

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.morkato.utility.MorkatoConfigLoader
import org.jetbrains.exposed.sql.Database
import org.morkato.api.entity.guild.Guild
import org.morkato.api.repository.GuildRepository
import org.morkato.api.repository.RepositoryCentral
import org.morkato.api.repository.SimpleRepositoryCentral
import org.morkato.api.repository.queries.GuildCreateQuery
import org.morkato.database.repository.DatabaseGuildRepository
import java.util.Properties
import java.util.concurrent.Executors

fun main(args: Array<String>) {
//  val settings: Properties = MorkatoConfigLoader.loadDefault()
//  val database: Database = DatabaseProvider.get(settings).getDatabase()
//  val validator: Validator = Validation.buildDefaultValidatorFactory().getValidator()
//  val central: SimpleRepositoryCentral = SimpleRepositoryCentral()
//  val repository: GuildRepository = DatabaseGuildRepository(central, database, validator)
//  central.setGuildRepository(repository)
//  val recovered: Guild = repository.fetch("123456789123456789")
//  print("Recovered: ")
//  println(recovered)
//  recovered.delete()
//  println("Deleted.")
}