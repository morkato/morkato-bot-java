package org.morkato.database.mapper;

import org.morkato.api.entity.ObjectEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectEntityMapper implements RowMapper<ObjectEntity> {
  @Override
  public ObjectEntity mapRow(ResultSet rs, int length) throws SQLException {
    return new ObjectEntity(rs.getString("id"));
  }
}
