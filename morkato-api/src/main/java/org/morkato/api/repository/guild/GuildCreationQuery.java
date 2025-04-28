package org.morkato.api.repository.guild;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.morkato.api.dto.GuildDTO;
import org.morkato.api.entity.MorkatoModelType;
import org.morkato.api.entity.guild.GuildId;
import org.morkato.api.validation.constraints.Mcisidv1Id;
import org.morkato.api.validation.constraints.MorkatoSnowflakeId;
import org.morkato.api.validation.groups.OnCreate;
import org.morkato.api.validation.groups.OnUpdate;

import java.math.BigDecimal;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuildCreationQuery implements GuildId {
  private String id;
  private String rpgId;
  private String rollCategoryId;
  private String offCategoryId;

  public GuildCreationQuery validate(Validator validator) {
    GuildDTO dto = GuildDTO.from(this);
    dto.validateForCreate(validator);
    return this;
  }
}
