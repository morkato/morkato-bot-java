package org.morkato.api.repository;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.flywaydb.core.Flyway;
import org.jetbrains.exposed.sql.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.database.DatabaseProvider;
import org.morkato.database.repository.DatabaseGuildRepository;

import javax.sql.DataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseGuildRepositoryTest extends GuildRepositoryTest {
  private DataSource datasource;
  private Flyway flyway;
  private Database database;
  private Validator validator;
  @Override
  protected GuildRepository getRepository() {
    datasource = DatabaseProvider.Companion.getDatasource(
      "morkato",
      "morkato",
      "jdbc:postgresql://localhost:8080/morkato-dev",
      "org.postgresql.Driver"
    );
    flyway = DatabaseProvider.Companion.getFlyway(datasource)
      .cleanDisabled(false)
      .load();
    flyway.clean();
    flyway.migrate();
    Database database = DatabaseProvider.Companion.getDatabase(datasource);
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    return new DatabaseGuildRepository(database, validator);
  }

  @Override
  protected void clean() {
    flyway.clean();
    flyway.migrate();
  }
}
