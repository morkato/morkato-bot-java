package org.morkato.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import java.util.Properties
import javax.sql.DataSource

class DatabaseProvider {
  companion object {
    private val LOGGER = LoggerFactory.getLogger(DatabaseProvider::class.java)
    fun getDatasource(user: String, password: String, url: String, driver: String): DataSource {
      LOGGER.info("Configuring database in url: {} with driver: {}.", url, driver);
      val hikari = HikariConfig()
      hikari.setJdbcUrl(url)
      hikari.setUsername(user)
      hikari.setPassword(password)
      hikari.setDriverClassName(driver)
      hikari.setMaximumPoolSize(10)
      return HikariDataSource(hikari)
    }
    fun getFlyway(datasource: DataSource): FluentConfiguration {
      return Flyway.configure()
        .dataSource(datasource)
        .locations("classpath:migration")
        .baselineOnMigrate(true)
    }
    fun getDatabase(datasource: DataSource): Database {
      return Database.connect(datasource)
    }
  }
}