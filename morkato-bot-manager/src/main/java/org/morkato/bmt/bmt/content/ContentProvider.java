package org.morkato.bmt.bmt.content;

public interface ContentProvider {
  ContentReference getContentReference(String key);
  ContentReference getContentReference(String key, String def);
  String getContent(String key, Object... placeholders);
  String getContent(String key);
}
