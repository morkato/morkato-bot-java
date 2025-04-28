package org.morkato.database.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.morkato.api.dto.RpgDTO;
import java.sql.SQLException;
import java.sql.ResultSet;

public class RpgTupleRowMapper implements RowMapper<RpgDTO> {
  @Override
  public RpgDTO mapRow(ResultSet rs, int length) throws SQLException {
    return new RpgDTO()
      .setId(rs.getString("id"))
      .setHumanInitialLife(rs.getBigDecimal("human_initial_life"))
      .setOniInitialLife(rs.getBigDecimal("oni_initial_life"))
      .setHybridInitialLife(rs.getBigDecimal("hybrid_initial_life"))
      .setBreathInitial(rs.getBigDecimal("breath_initial"))
      .setBloodInitial(rs.getBigDecimal("blood_initial"))
      .setAbilityRoll(rs.getBigDecimal("ability_roll"))
      .setFamilyRoll(rs.getBigDecimal("family_roll"));
  }
}
