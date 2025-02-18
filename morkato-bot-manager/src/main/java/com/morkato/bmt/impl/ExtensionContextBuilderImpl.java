package com.morkato.bmt.impl;

import com.morkato.bmt.Extension;
import com.morkato.bmt.ExtensionContextBuilder;
import org.jetbrains.annotations.NotNull;

public class ExtensionContextBuilderImpl implements ExtensionContextBuilder {
  private Extension extension;

  public ExtensionContextBuilderImpl(
    @NotNull Extension extension
  ) {
    this.extension = extension;
  }

  @Override
  public Extension getRunningExtension() {
    return extension;
  }
}
