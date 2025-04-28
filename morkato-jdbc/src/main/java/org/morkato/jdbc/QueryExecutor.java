package org.morkato.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public interface QueryExecutor {
  boolean isQueryPresent();
  <T> T queryForObject(JdbcTemplate jdbc, RowMapper<T> mapper, Object... values);
  int update(JdbcTemplate jdbc, Object... values);
}
