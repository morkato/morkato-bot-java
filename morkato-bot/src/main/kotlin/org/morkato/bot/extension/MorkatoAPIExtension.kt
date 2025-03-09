package org.morkato.bot.extension

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.jetbrains.exposed.sql.Database
import org.morkato.api.repository.ArtRepository
import org.morkato.api.repository.GuildRepository
import org.morkato.api.repository.SimpleRepositoryCentral
import org.morkato.bmt.context.ApplicationContextBuilder
import org.morkato.bmt.annotation.RegistryExtension
import org.morkato.bmt.extensions.BaseExtension
import org.morkato.database.DatabaseProvider
import org.morkato.database.repository.DatabaseArtRepository
import org.morkato.database.repository.DatabaseGuildRepository
import org.morkato.database.repository.GuildRepositoryProxy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.sql.DataSource

@RegistryExtension
class MorkatoAPIExtension : BaseExtension() {
  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(MorkatoAPIExtension::class.java)
  }
  override fun start(app: ApplicationContextBuilder) {
    val central: SimpleRepositoryCentral = SimpleRepositoryCentral()
    app.inject(central)
    try {
      val datasource: DataSource = DatabaseProvider.getDatasource(app.getProperties())
      DatabaseProvider.getFlyway(datasource).migrate()
      val database = Database.connect(datasource)
      val validator: Validator = Validation.buildDefaultValidatorFactory().getValidator()
      val guilds: GuildRepository = GuildRepositoryProxy(DatabaseGuildRepository(central, database, validator))
      val arts: ArtRepository = DatabaseArtRepository(central, database, validator)
      central.setGuildRepository(guilds)
      central.setArtRepository(arts)
    } catch (exc: Exception) {
      LOGGER.warn("Failhed to start database. An error occurred: {}.", exc.javaClass.name, exc)
    }
  }
}