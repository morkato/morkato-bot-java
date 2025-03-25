package org.morkato.api.entity.user;

public enum UserType {
  HUMAN("HUMAN"),
  ONI("ONI"),
  HYBRID("HYBRID");
  private final String value;

  private UserType(String value) {
    this.value = value;
  }

  public String getRawValue() {
    return value;
  }
}
