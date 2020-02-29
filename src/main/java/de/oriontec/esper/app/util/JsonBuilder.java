package de.oriontec.esper.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonBuilder {

  private static ObjectMapper mapper = new ObjectMapper();

  public static String build(final boolean success, final int code, final Object message) {
    try {
      Map<String, Object> map = new HashMap<>();
      map.put("success", success);
      if (success) {
        map.put("payload", message);
      } else {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("code", code);
        errorMap.put("message", message);
        map.put("error", errorMap);
      }
      return mapper.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

}
