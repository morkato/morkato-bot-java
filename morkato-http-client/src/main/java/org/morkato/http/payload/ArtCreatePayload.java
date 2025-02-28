package org.morkato.http.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.morkato.api.art.ArtType;
import org.morkato.api.repository.queries.ArtCreationQuery;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtCreatePayload(
  @JsonProperty(required = true)
  String name,
  @JsonProperty(required = true)
  ArtType type,
  String description,
  String banner
) {
  public static ArtCreatePayload from(ArtCreationQuery query) {
    return new ArtCreatePayload(
      query.name(),
      query.type(),
      query.description(),
      query.banner()
    );
  }
}
