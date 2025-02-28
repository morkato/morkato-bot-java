package org.morkato.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.morkato.api.art.Art;
import org.morkato.api.art.ArtType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonIgnoreProperties(ignoreUnknown=true)
public record ArtDTO(
  @Nonnull
  String guildId,
  @Nonnull
  String id,
  @Nonnull
  String name,
  @Nonnull
  ArtType type,
  @Nullable
  String description,
  @Nullable
  String banner
) {
  public static ArtDTO fromArt(Art art) {
    return new ArtDTO(
      art.getGuildId(),
      art.getId(),
      art.getName(),
      art.getType(),
      art.getDescription(),
      art.getBanner()
    );
  }
}
