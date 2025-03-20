package org.morkato.bmt.bot.extension

import jakarta.validation.Validation
import org.morkato.bmt.api.repository.GuildRepository
import org.morkato.bmt.api.repository.SimpleRepositoryCentral
import org.morkato.bmt.api.repository.TrainerRepository
import org.morkato.bmt.api.repository.queries.TrainerCreationQuery
import org.morkato.bmt.annotation.RegistryExtension
import org.morkato.bmt.bmt.context.ApplicationContextBuilder
import org.morkato.bmt.bmt.extensions.BaseExtension
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import java.math.BigDecimal

@RegistryExtension
class MorkatoAPIExtension : BaseExtension() {
  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(MorkatoAPIExtension::class.java)
    /* TODO: Está extensão será removida. Farei um banco virtual por agora, pois criar no físico (PostgreSQL) demanda mais tempo. Isso é temporário. */
    val builtinTrainers: Set<TrainerCreationQuery> = setOf(
      TrainerCreationQuery(
        "971803172056219728",
        "Novato",
        131162,
        BigDecimal.valueOf(1000),
        BigDecimal.valueOf(1000),
        BigDecimal.valueOf(0),
        "⚔",
        "https://c.tenor.com/fWzS8rODMGwAAAAd/tenor.gif",
        0
      )
    )
  }

  override fun start(app: ApplicationContextBuilder) {
    val central = SimpleRepositoryCentral()
    val validator = Validation.buildDefaultValidatorFactory().validator
    val guilds = GuildRepository.createInMemory(central, validator)
    val trainers = TrainerRepository.createInMemory(validator)
    central.setTrainerRepository(trainers)
    central.setGuildRepository(guilds)
    guilds.fetch("971803172056219728")
    TrainerRepository.createAll(trainers, builtinTrainers);
    app.inject(central)
  }
}