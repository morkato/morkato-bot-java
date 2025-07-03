package org.morkato.mcbmt.commands.rules;

public interface SlashMapperData {
  String getAsString(String key);
  long getAsLong(String key);

  String getAsStringOrNull(String key);
  long getAsLongOrNull(String key);
}
