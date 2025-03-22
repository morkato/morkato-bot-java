package org.morkato.bmt.content;

import java.util.HashMap;
import java.util.Map;

public class ContentProviderImpl implements ContentProvider {
  private final Map<String, ContentReference> references = new HashMap<>();

  @Override
  public ContentReference getContentReference(String key) {
    return null;
  }

  @Override
  public ContentReference getContentReference(String key, String def) {
    return null;
  }

  @Override
  public String getContent(String key, Object... placeholders) {
    return "";
  }

  @Override
  public String getContent(String key) {
    return "";
  }

  public void setContent(String key, String value) {

  }
}
