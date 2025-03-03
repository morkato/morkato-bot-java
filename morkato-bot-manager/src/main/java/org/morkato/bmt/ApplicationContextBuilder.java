package org.morkato.bmt;

import org.morkato.bmt.components.Extension;

import java.util.Properties;

public interface ApplicationContextBuilder {
  Extension getRunningExtension();
  Properties getProperties();
  String getProperty(String key, String def);
  String getProperty(String key);
  <P> void inject(P value);
}
