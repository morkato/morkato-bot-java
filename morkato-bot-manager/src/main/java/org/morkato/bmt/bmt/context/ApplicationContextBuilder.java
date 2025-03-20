package org.morkato.bmt.bmt.context;

import org.morkato.bmt.bmt.extensions.Extension;

import java.util.Properties;

public interface ApplicationContextBuilder {
  Extension getRunningExtension();
  Properties getProperties();
  String getProperty(String key, String def);
  String getProperty(String key);
  <P> void inject(P value);
}
