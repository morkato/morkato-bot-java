package org.morkato.api.entity.art;

public enum ArtType {
  RESPIRATION("RESPIRATION"),
  KEKKIJUTSU("KEKKIJUTSU"),
  FIGHTING_STYLE("FIGHTING_STYLE");
  private final String value;
  ArtType(String vl) {
    this.value = vl;
  }
  public String getRawValue() {
    return this.value;
  }
}
