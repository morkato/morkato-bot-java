package org.morkato.database.repository

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import org.morkato.api.repository.GuildRepository
import org.morkato.api.repository.GuildRepositoryTest
import org.morkato.database.DatabaseProvider
import org.morkato.utility.MorkatoConfigLoader
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseGuildRepositoryTest : GuildRepositoryTest() {
  companion object {
    const val THREAD_POOL_SIZE: Int = 5
  }
  private val executor: ExecutorService
  private val settings: Properties
  private val provider: DatabaseProvider
  private val validator: Validator
  init {
    settings = MorkatoConfigLoader.loadDefault()
    executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    provider = DatabaseProvider.get(settings)
    validator = Validation.buildDefaultValidatorFactory().getValidator()
  }

  override fun cleanRepository() {
    provider.clean()
    provider.migrate()
  }

  override fun snapshotRepository() {
    
  }

  override fun createRepository() : GuildRepository {
    return DatabaseGuildRepository(executor, provider.getDatabase(), validator)
  }

  @AfterAll
  fun closeThreadPool() {
    println("Fechando o ExecutorService")
    this.executor.shutdown()
  }

//  @Test
//  @DisplayName("Testando erro de validação de ID da guilda.")
//  fun guildIdIsInvalid() {
//    this.testGuildIdIsInvalid()
//  }
//
//  @Test
//  @DisplayName("Testando erro de ID não existente")
//  fun guildIsNotFound() {
//    this.testGuildNotFound()
//  }
}