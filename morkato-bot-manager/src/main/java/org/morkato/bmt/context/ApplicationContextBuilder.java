package org.morkato.bmt.context;

import org.morkato.boot.Extension;

public interface ApplicationContextBuilder {
  Extension<?> getRunningExtension();
  <P> void inject(P value);
}
