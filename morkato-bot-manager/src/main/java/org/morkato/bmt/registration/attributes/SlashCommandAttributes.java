package org.morkato.bmt.registration.attributes;

import java.util.Objects;

public class SlashCommandAttributes {
  private final String name;
  private final String description;

  public SlashCommandAttributes(String name, String description) {
    this.name = Objects.requireNonNull(name);
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription(){
    return description;
  }
}
