package org.morkato.bot.extension

import jakarta.validation.Validation
import org.jetbrains.exposed.sql.Database
import org.morkato.api.repository.SimpleRepositoryCentral
import org.morkato.bmt.annotation.ApplicationProperty
import org.morkato.bmt.annotation.RegistryExtension
import org.morkato.bmt.context.ApplicationContextBuilder
import org.morkato.bmt.extensions.BaseExtension
import org.morkato.database.DatabaseProvider
import org.morkato.database.repository.DatabaseGuildRepository

@RegistryExtension
class MorkatoAPIExtension : BaseExtension() {
//  companion object {
//    val LOGGER: Logger = LoggerFactory.getLogger(MorkatoAPIExtension::class.java)
//    /* TODO: Está extensão será removida. Farei um banco virtual por agora, pois criar no físico (PostgreSQL) demanda mais tempo. Isso é temporário. */
//    val builtinTrainers: Set<TrainerCreationQuery> = setOf(
//      TrainerCreationQuery(
//        "971803172056219728",
//        "Novato",
//        131162,
//        BigDecimal.valueOf(1000),
//        BigDecimal.valueOf(1000),
//        BigDecimal.valueOf(0),
//        "⚔",
//        "https://c.tenor.com/fWzS8rODMGwAAAAd/tenor.gif",
//        0
//      )
//    )
//  }

  @ApplicationProperty("morkato.datasource.url")
  lateinit var dbUrl: String
  @ApplicationProperty("morkato.datasource.user")
  lateinit var dbUser: String
  @ApplicationProperty("morkato.datasource.password")
  lateinit var dbPassword: String

  override fun start(app: ApplicationContextBuilder) {
    val central = SimpleRepositoryCentral()
    app.inject(central)
    val validator = Validation.buildDefaultValidatorFactory().getValidator()
    val datasource = DatabaseProvider.getDatasource(app.getProperties())
    val flyway = DatabaseProvider.getFlyway(datasource)
    flyway.migrate()
    val database = Database.connect(datasource)
    central.setGuildRepository(DatabaseGuildRepository(database, validator))
  }
}