package org.morkato.api.entity.impl.art;

import org.morkato.api.dto.ArtDTO;
import org.morkato.api.entity.art.ArtPayload;
import org.morkato.api.entity.art.ArtType;
import lombok.Getter;
import java.util.Objects;

@Getter
public class ArtPayloadImpl implements ArtPayload {
  private final String guildId;
  private final String id;
  private final String name;
  private final ArtType type;
  private final String description;
  private final String banner;

  public ArtPayloadImpl(
    String guildId,
    String id,
    String name,
    ArtType type,
    String description,
    String banner
  ) {
    this.guildId = Objects.requireNonNull(guildId);
    this.id = Objects.requireNonNull(id);
    this.name = Objects.requireNonNull(name);
    this.type = Objects.requireNonNull(type);
    this.description = description;
    this.banner = banner;
  }

  public ArtPayloadImpl(ArtDTO dto) {
    this(
      dto.getGuildId(),
      dto.getId(),
      dto.getName(),
      dto.getType(),
      dto.getDescription(),
      dto.getBanner()
    );
  }
}
