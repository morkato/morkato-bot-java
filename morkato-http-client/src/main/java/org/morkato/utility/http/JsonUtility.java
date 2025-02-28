package org.morkato.utility.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.entity.StringEntity;
import org.morkato.utility.JacksonObjectMapper;

import java.nio.charset.StandardCharsets;

public class JsonUtility {
  public static StringEntity toJsonEntity(Object object) throws JsonProcessingException {
    String json = JacksonObjectMapper.toJson(object);
    StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
    entity.setContentType("application/json");
    entity.setContentEncoding(StandardCharsets.UTF_8.displayName());
    return entity;
  }
}
