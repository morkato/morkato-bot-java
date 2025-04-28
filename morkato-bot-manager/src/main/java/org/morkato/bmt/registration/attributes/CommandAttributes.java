package org.morkato.bmt.registration.attributes;

import java.util.Objects;

public class CommandAttributes {
  private final String[] aliases;
  private final String description;

  public CommandAttributes(String[] aliases, String description) {
    this.aliases = Objects.requireNonNull(aliases);
    this.description = description;
  }

  public String[] getAliases() {
    return aliases;
  }

  public String getDescription() {
    return description;
  }
}
