package org.morkato.bmt.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import java.util.Properties
import javax.sql.DataSource

class DatabaseProvider {
  companion object {
    private val LOGGER = LoggerFactory.getLogger(DatabaseProvider::class.java)
    fun getDatasource(properties: Properties): DataSource {
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
      return HikariDataSource(hikari)
    }
    fun getFlyway(datasource: DataSource): Flyway {
      return Flyway.configure()
        .dataSource(datasource)
        .locations("classpath:migration")
        .baselineOnMigrate(true)
        .load()
    }
  }
}