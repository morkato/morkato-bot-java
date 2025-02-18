package com.morkato.service.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.postgresql.Driver;
import javax.annotation.Nonnull;
import javax.sql.DataSource;

public class DatabaseManager {
  private final HikariConfig hikari = new HikariConfig();
  private final HikariDataSource source;
  private final String dbUrl;
  private final String dbUser;
  private final String dbPassword;
  private final Flyway flyway;
  public DatabaseManager(
    @Nonnull String url,
    @Nonnull String user,
    @Nonnull String password
  ){
    this.dbUrl = url;
    this.dbUser = user;
    this.dbPassword = password;
    this.hikari.setJdbcUrl(url);
    this.hikari.setUsername(user);
    this.hikari.setPassword(password);
    this.hikari.setDriverClassName(Driver.class.getName());
    this.hikari.setMaximumPoolSize(10);
    this.hikari.setMinimumIdle(2);
    this.hikari.setIdleTimeout(30000);
    this.hikari.setConnectionTimeout(30000);
    this.hikari.setMaxLifetime(1800000);
    this.source = new HikariDataSource(hikari);
    this.flyway = Flyway.configure()
      .dataSource(source)
      .baselineOnMigrate(true)
      .locations("classpath:database")
      .load();
  }
  public void migrate() {
    this.flyway.migrate();
  }
  public DataSource getDataSource() {
    return this.source;
  }
}
