package org.morkato.bmt;

import org.morkato.bmt.components.Extension;

public interface ApplicationContextBuilder {
  Extension getRunningExtension();
  String getProperty(String key, String def);
  String getProperty(String key);
  <P> void inject(P value);
}
