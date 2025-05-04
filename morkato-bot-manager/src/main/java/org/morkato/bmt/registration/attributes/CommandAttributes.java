package org.morkato.bmt.registration.attributes;

import java.util.Objects;

public class CommandAttributes {
  private final String name;
  private final String[] aliases;
  private final String description;

  public CommandAttributes(String name, String[] aliases, String description) {
    this.name = Objects.requireNonNull(name);
    this.aliases = Objects.requireNonNull(aliases);
    this.description = description;
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
}
