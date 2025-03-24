package org.morkato.bot.extension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.flywaydb.core.Flyway;
import org.jetbrains.exposed.sql.Database;
import org.morkato.api.repository.RepositoryCentral;
import org.morkato.api.repository.SimpleRepositoryCentral;
import org.morkato.bmt.annotation.ApplicationProperty;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.context.ApplicationContextBuilder;
import org.morkato.bmt.extensions.BaseExtension;
import org.morkato.bmt.extensions.Extension;
import org.morkato.database.DatabaseProvider;
import org.morkato.database.repository.DatabaseArtRepository;
import org.morkato.database.repository.DatabaseGuildRepository;

import javax.sql.DataSource;

@Component
public class MorkatoAPIExtension2 extends BaseExtension implements Extension {
  @ApplicationProperty("morkato.datasource.user")
  private String dbUser;
  @ApplicationProperty("morkato.datasource.password")
  private String dbPassword;
  @ApplicationProperty("morkato.datasource.url")
  private String dbUrl;
  @ApplicationProperty("morkato.datasource.driver")
  private String dbDriver;

  @Override
  public void start(ApplicationContextBuilder application) throws Throwable {
    final SimpleRepositoryCentral central = new SimpleRepositoryCentral();
    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    final DataSource datasource = DatabaseProvider.Companion.getDatasource(
      dbUser,
      dbPassword,
      dbUrl,
      dbDriver
    );
    final Flyway flyway = DatabaseProvider.Companion.getFlyway(datasource);
    flyway.migrate();
    final Database database = DatabaseProvider.Companion.getDatabase(datasource);
    application.inject(central);
    central.setGuildRepository(new DatabaseGuildRepository(database, validator));
    central.setArtRepository(new DatabaseArtRepository(database, validator));
  }
}
