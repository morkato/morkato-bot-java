package org.morkato.mcbmt.startup.attributes;

import java.util.Objects;

public class SlashCommandAttributes {
  private final String name;
  private final String description;
  private final int flags;
  private final Class<?> argumentClass;

  public SlashCommandAttributes(String name, String description, int flags, Class<?> argumentClass) {
    this.name = Objects.requireNonNull(name);
    this.description = description;
    this.flags = flags;
    this.argumentClass = Objects.requireNonNull(argumentClass);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getFlags() {
    return flags;
  }

  public Class<?> getArgumentClass() {
    return argumentClass;
  }
}
