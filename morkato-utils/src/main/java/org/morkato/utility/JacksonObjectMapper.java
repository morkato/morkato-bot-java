package org.morkato.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectMapper {
  private static final ObjectMapper mapper = new ObjectMapper();
  public static <T> T fromJson(String text, Class<T> parser) throws JsonProcessingException, JsonMappingException {
    return mapper.readValue(text, parser);
  }
  public static String toJson(Object object) throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }
}
