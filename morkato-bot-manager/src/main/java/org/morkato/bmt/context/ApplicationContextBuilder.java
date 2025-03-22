package org.morkato.bmt.context;

import org.morkato.bmt.extensions.Extension;

import java.util.Properties;

public interface ApplicationContextBuilder {
  Extension getRunningExtension();
  Properties getProperties();
  String getProperty(String key, String def);
  String getProperty(String key);
  <P> void inject(P value);
}
