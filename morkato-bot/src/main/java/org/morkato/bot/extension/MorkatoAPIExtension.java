package org.morkato.bot.extension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.flywaydb.core.Flyway;
import org.morkato.api.repository.guild.GuildRepository;
import org.morkato.boot.annotation.ApplicationProperty;
import org.morkato.boot.annotation.DefaultValue;
import org.morkato.boot.ApplicationContext;
import org.morkato.bmt.context.BotContext;
import org.morkato.boot.BaseExtension;
import org.morkato.database.PsqlRepositoryStatement;
import org.morkato.database.repository.guild.PsqlGuildRepository;
import org.morkato.jdbc.QueryLoader;
import org.morkato.jdbc.ReflectionQueryLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class MorkatoAPIExtension extends BaseExtension<BotContext> {
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @ApplicationProperty("morkato.datasource.url")
  private String dbUrl;
  @ApplicationProperty("morkato.datasource.user")
  private String dbUser;
  @ApplicationProperty("morkato.datasource.password")
  private String dbPassword;
  @ApplicationProperty("morkato.datasource.driver")
  private String dbDriver;
  @ApplicationProperty("morkato.datasource.migrationPath")
  @DefaultValue("classpath:migration")
  private String migrationPath;
  @ApplicationProperty("morkato.datasource.queriesPath")
  @DefaultValue("queries")
  private String queriesPath;

  public DataSource connect() {
    final DriverManagerDataSource source = new DriverManagerDataSource();
    source.setDriverClassName(dbDriver);
    source.setUrl(dbUrl);
    source.setUsername(dbUser);
    source.setPassword(dbPassword);
    return source;
  }

  @Override
  public void start(ApplicationContext<BotContext> application) {
    final DataSource source = this.connect();
    final PsqlRepositoryStatement statement = new PsqlRepositoryStatement(source, validator);
    final QueryLoader loader = new QueryLoader(queriesPath);
    final ReflectionQueryLoader reflection = new ReflectionQueryLoader(loader);
    final Flyway flyway = Flyway.configure()
      .dataSource(source)
      .locations(migrationPath)
      .load();
    flyway.migrate();
    GuildRepository repository = new PsqlGuildRepository(statement);
    reflection.writeAll(repository);
    application.inject(repository);
  }
}
