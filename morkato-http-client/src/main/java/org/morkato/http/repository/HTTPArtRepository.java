package org.morkato.http.repository;

import org.apache.http.entity.StringEntity;
import org.morkato.api.repository.queries.ArtCreationQuery;
import org.morkato.api.repository.queries.ArtFetchQuery;
import org.morkato.api.repository.queries.ArtUpdateQuery;
import org.morkato.api.repository.ArtRepository;
import org.morkato.api.art.Art;
import org.morkato.http.model.HTTPArt;
import org.morkato.http.HTTPClient;
import org.morkato.http.HTTPRoutes;
import org.morkato.http.dto.ArtDTO;
import org.morkato.http.payload.ArtCreatePayload;
import org.morkato.utility.http.JsonUtility;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.Objects;

public class HTTPArtRepository implements ArtRepository {
  public final HTTPClient client;
  public HTTPArtRepository(HTTPClient client) {
    Objects.requireNonNull(client);
    this.client = client;
  }

  private HTTPArt fromDTO(ArtDTO dto) {
    return new HTTPArt(client, dto);
  }

  @Override
  @Nonnull
  public Future<Art[]> fetchAll(String guildId) {
    return this.client.get(ArtDTO[].class)
      .setRoute(HTTPRoutes.ALL_ARTS)
      .setArgs(guildId)
      .queue()
      .thenApply(dtos -> Arrays.stream(dtos).map(this::fromDTO).toArray(Art[]::new));
  }

  @Override
  @Nonnull
  public Future<Art> fetch(ArtFetchQuery query) {
    return this.client.get(ArtDTO.class)
      .setRoute(HTTPRoutes.UNIQUE_ART)
      .setArgs(query.getGuildId(), query.getId())
      .queue()
      .thenApply(dto -> new HTTPArt(client, dto));
  }

  @Override
  @Nonnull
  public Future<Art> create(ArtCreationQuery query) {
    try {
      StringEntity json = JsonUtility.toJsonEntity(ArtCreatePayload.from(query));
      return this.client.post(ArtDTO.class)
        .setRoute(HTTPRoutes.ALL_ARTS)
        .setArgs(query.guildId())
        .setEntity(json)
        .queue()
        .thenApply(dto -> new HTTPArt(client, dto));
    } catch (Throwable exception) {
      return CompletableFuture.failedFuture(exception);
    }
  }

  @Override
  @Nonnull
  public Future<Art> update(ArtUpdateQuery query) {
    return null;
  }

  @Override
  @Nonnull
  public Future<Art> delete(ArtFetchQuery query) {
    return null;
  }
}
