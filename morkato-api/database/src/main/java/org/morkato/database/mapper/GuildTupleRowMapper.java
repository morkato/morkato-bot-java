package org.morkato.database.mapper;

import org.morkato.api.dto.GuildDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GuildTupleRowMapper implements RowMapper<GuildDTO> {
  @Override
  public GuildDTO mapRow(ResultSet rs, int length) throws SQLException {
    return new GuildDTO()
      .setId(rs.getString("id"))
      .setRpgId(rs.getString("rpg_id"))
      .setRollCategoryId(rs.getString("roll_category_id"))
      .setOffCategoryId(rs.getString("off_category_id"));
  }
}
