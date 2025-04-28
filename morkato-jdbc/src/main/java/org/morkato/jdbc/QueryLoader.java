package org.morkato.jdbc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class QueryLoader {
  private final String offset;

  public QueryLoader(String offset) {
    this.offset = offset;
  }

  public QueryExecutor loadQuery(String resource) throws IOException {
    final InputStream stream = ClassLoader.getSystemResourceAsStream(Paths.get(offset, resource).toString());
    if (Objects.isNull(stream))
      return new NullQueryExecutor();
    final String query = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    return new LoadedQueryExecutor(query);
  }
}
