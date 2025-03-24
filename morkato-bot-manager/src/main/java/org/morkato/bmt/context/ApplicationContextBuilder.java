package org.morkato.bmt.context;

import org.morkato.bmt.extensions.Extension;

import java.util.Properties;

public interface ApplicationContextBuilder {
  Extension getRunningExtension();
  <P> void inject(P value);
}
