package org.morkato.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class NullQueryExecutor implements QueryExecutor {
  @Override
  public boolean isQueryPresent() {
    return false;
  }

  @Override
  public <T> T queryForObject(JdbcTemplate jdbc, RowMapper<T> mapper, Object... values) {
    throw new RuntimeException("Impossible run query. Query is null.");
  }

  @Override
  public int update(JdbcTemplate jdbc, Object... values){
    throw new RuntimeException("Impossible run query. Query is null.");
  }
}
