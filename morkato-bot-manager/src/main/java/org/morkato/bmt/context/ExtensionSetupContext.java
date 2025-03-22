package org.morkato.bmt.context;

import org.morkato.bmt.components.Command;

public interface ExtensionSetupContext {
  void setCurrentCommand(Class<? extends Command<?>> command);
  void setCommandName(String name, Class<? extends Command<?>> command);
  void setCommandName(String name);
}
