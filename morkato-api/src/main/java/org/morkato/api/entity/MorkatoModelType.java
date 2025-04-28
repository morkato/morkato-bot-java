package org.morkato.api.entity;

import org.morkato.utility.mcisid.ModelType;

public enum MorkatoModelType implements ModelType {
  GENERIC(0),
  RPG(1),
  ART(15),
  ATTACK(16),
  ABILITY(17),
  FAMILY(18);
  private final int value;
  private MorkatoModelType(int value) {
    this.value = value & 0x3F;
  }
  @Override
  public int getRawValue() {
    return value;
  }
}
