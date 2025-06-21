package org.morkato.bmt.startup.attributes;

import java.util.Objects;

public class CommandAttributes {
  private final String name;
  private final String[] aliases;
  private final String description;
  private final Class<?> argumentClass;

  public CommandAttributes(String name, String[] aliases, String description, Class<?> argumentClass) {
    this.name = Objects.requireNonNull(name);
    this.aliases = Objects.requireNonNull(aliases);
    this.description = description;
    this.argumentClass = Objects.requireNonNull(argumentClass);
  }

  public String getName() {
    return name;
  }

  public String[] getAliases() {
    return aliases;
  }

  public String getDescription() {
    return description;
  }

  public Class<?> getArgumentClass() {
    return argumentClass;
  }
}
