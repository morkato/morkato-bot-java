package org.morkato.http.model;

import org.morkato.api.art.ArtEditBuilder;
import org.morkato.api.art.ArtType;
import org.morkato.api.art.Art;

import org.morkato.http.HTTPClient;
import org.morkato.http.dto.ArtDTO;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Future;

public class HTTPArt implements Art {
  private final HTTPClient client;
  private String guildId;
  private String id;
  private String name;
  private ArtType type;
  private String description;
  private String banner;

  public HTTPArt(HTTPClient client, ArtDTO dto) {
    this.client = client;
    this.guildId = dto.guildId();
    this.id = dto.id();
    this.name = dto.name();
    this.type = dto.type();
    this.description = dto.description();
    this.banner = dto.banner();
  }
  @Nonnull
  @Override
  public String getGuildId() {
    return this.guildId;
  }

  @Nonnull
  @Override
  public String getId() {
    return this.id;
  }

  @Nonnull
  @Override
  public String getName() {
    return this.name;
  }

  @Nonnull
  @Override
  public ArtType getType() {
    return this.type;
  }

  @Nullable
  @Override
  public String getDescription() {
    return this.description;
  }

  @Nullable
  @Override
  public String getBanner() {
    return this.banner;
  }

  @Override
  public Future<Art> delete(){
    return null;
  }

  @Override
  public ArtEditBuilder doUpdate(){
    return null;
  }
}
