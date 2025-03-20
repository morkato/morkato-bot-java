package org.morkato.bmt.bmt.errors;

import org.morkato.bmt.bmt.extensions.Extension;
import org.jetbrains.annotations.NotNull;

public class ExtensionAlreadyRegistered extends MorkatoBotException {
  private Extension extension;
  public ExtensionAlreadyRegistered(
    @NotNull Extension extension
  ) {
    super("Extension (class):" + extension.getClass().getName() + " already registered.");
    this.extension = extension;
  }
  public Extension getExtension() {
    return this.extension;
  }
}
