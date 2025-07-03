package org.morkato.mcbmt.startup.attributes;

import java.util.Objects;

public class ActionAttributes {
  private final String name;

  public ActionAttributes(String name) {
    this.name = Objects.requireNonNull(name);
  }

  public String getName() {
    return name;
  }
}
