package org.morkato.database;

import jakarta.validation.Validator;
import org.morkato.api.dto.DefaultDTO;
import org.morkato.api.exception.repository.RepositoryException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PsqlRepositoryStatement{
  private final JdbcTemplate jdbc;
  private final DataSourceTransactionManager manager;
  private final DataSource source;
  private final Validator validator;


  public PsqlRepositoryStatement(DataSource source, Validator validator) {
    this.source = source;
    this.jdbc = new JdbcTemplate(source);
    this.manager = new DataSourceTransactionManager(source);
    this.validator = validator;
  }

  public void assertTransactionActive() {
    final Connection connection = DataSourceUtils.getConnection(source);
    try {
      if (connection.getAutoCommit())
        throw new IllegalStateException("Cannot proceed without an active transaction (autocommit is enabled).");
    } catch (SQLException exc) {
      throw new RepositoryException("Failed to verify transaction status.");
    }
  }

  public JdbcTemplate getJdbc() {
    return jdbc;
  }

  public DefaultTransactionDefinition newTransactionDefinition() {
    return new DefaultTransactionDefinition();
  }

  public TransactionStatus getTransactionStatus(DefaultTransactionDefinition def) {
    return this.manager.getTransaction(def);
  }

  public void commit(TransactionStatus status) {
    manager.commit(status);
  }

  public void rollback(TransactionStatus status) {
    manager.rollback(status);
  }

  public void validateForCreate(DefaultDTO<?> dto) {
    dto.validateForCreate(validator);
  }
}
