package org.morkato.bmt.context;

import org.morkato.bmt.components.Command;

public interface ExtensionSetupContext {
  void setName(String name, Class<? extends Command<?>> command);
}
