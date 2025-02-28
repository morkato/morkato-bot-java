package org.morkato.bmt.impl;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.context.ExtensionSetupContext;

import java.util.HashMap;
import java.util.Map;

public class ExtensionSetupContextImpl implements ExtensionSetupContext {
  private final Map<String, Class<? extends Command<?>>> names = new HashMap<>();

  public Map<String, Class<? extends Command<?>>> getNames() {
    return this.names;
  }

  @Override
  public void setName(String name, Class<? extends Command<?>> command) {
    this.names.put(name, command);
  }
}
