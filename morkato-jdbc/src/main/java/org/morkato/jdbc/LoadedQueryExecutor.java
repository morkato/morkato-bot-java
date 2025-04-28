package org.morkato.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Objects;

public class LoadedQueryExecutor implements QueryExecutor {
  private final String sql;
  public LoadedQueryExecutor(String sql) {
    this.sql = Objects.requireNonNull(sql);
  }

  @Override
  public boolean isQueryPresent() {
    return true;
  }

  @Override
  public <T> T queryForObject(JdbcTemplate jdbc, RowMapper<T> mapper, Object... values) {
    return jdbc.queryForObject(sql, mapper, values);
  }

  @Override
  public int update(JdbcTemplate jdbc, Object... values){
    return jdbc.update(sql, values);
  }
}
