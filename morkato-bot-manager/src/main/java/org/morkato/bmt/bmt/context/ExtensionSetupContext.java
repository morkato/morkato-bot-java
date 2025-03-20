package org.morkato.bmt.bmt.context;

import org.morkato.bmt.bmt.components.Command;

public interface ExtensionSetupContext {
  void setCurrentCommand(Class<? extends Command<?>> command);
  void setCommandName(String name, Class<? extends Command<?>> command);
  void setCommandName(String name);
}
