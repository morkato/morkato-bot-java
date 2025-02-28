package org.morkato.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.CleanResult
import org.flywaydb.core.api.output.MigrateResult
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import java.util.Properties
import javax.sql.DataSource

class DatabaseProvider(
  private val dataSource: DataSource,
  private val flyway: Flyway,
  private val database: Database
) {
  companion object {
    private val LOGGER = LoggerFactory.getLogger(DatabaseProvider::class.java)
    fun get(properties: Properties) : DatabaseProvider {
      val user: String = properties.getProperty("morkato.database.user")
      val password: String = properties.getProperty("morkato.database.password")
      val url: String = properties.getProperty("morkato.database.url")
      val driver: String = properties.getProperty("morkato.database.driver")
      LOGGER.info("Configuring database in url: {} with driver: {}.", url, driver);
      val hikari = HikariConfig()
      hikari.setJdbcUrl(url)
      hikari.setUsername(user)
      hikari.setPassword(password)
      hikari.setDriverClassName(driver)
      hikari.setMaximumPoolSize(10)
      val datasource = HikariDataSource(hikari)
      val flyway: Flyway = Flyway.configure()
        .dataSource(datasource)
        .locations("classpath:migration")
        .cleanDisabled(false)
        .baselineOnMigrate(true)
        .load()
      flyway.migrate();
      val database: Database = Database.connect(datasource)
      return DatabaseProvider(datasource, flyway, database)
    }
  }

  fun getDatabase() : Database {
    return database
  }

  fun clean() : CleanResult {
    return flyway.clean()
  }

  fun migrate() : MigrateResult {
    return flyway.migrate()
  }
}