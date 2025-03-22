package org.morkato.bmt.impl;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.ExtensionSetupContext;

import java.util.HashMap;
import java.util.Map;

public class ExtensionSetupContextImpl implements ExtensionSetupContext {
  private final Map<String, Class<? extends Command<?>>> names = new HashMap<>();
  private Class<? extends Command<?>> selectedCommand = null;

  public Map<String, Class<? extends Command<?>>> getNames() {
    return this.names;
  }


  @Override
  public void setCurrentCommand(Class<? extends Command<?>> command) {
    selectedCommand = command;
  }

  @Override
  public void setCommandName(String name, Class<? extends Command<?>> command) {
    this.names.put(name, command);
  }

  @Override
  public void setCommandName(String name) {
    if (selectedCommand == null)
      return;
    this.setCommandName(name, selectedCommand);
  }
}
