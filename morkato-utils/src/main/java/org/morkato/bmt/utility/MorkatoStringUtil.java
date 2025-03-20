package org.morkato.bmt.utility;

import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

public class MorkatoStringUtil {
  public static String toMorkatoKey(String text) {
    if (Objects.isNull(text))
      return null;
    text = text.strip();
    text = text.replaceAll("\\s+", "-");
    text = text.toLowerCase();
    text = StringUtils.stripAccents(text);
    return text;
  }
}
