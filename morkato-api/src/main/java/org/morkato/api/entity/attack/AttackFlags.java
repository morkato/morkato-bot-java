package org.morkato.api.entity.attack;

public final class AttackFlags {
  private enum AttackFlag {
    UNAVOIABLE(1 << 1),
    INDEFENSIBLE(1 << 2),
    AREA(1 << 3),
    NOT_COUNTER_ATTACKABLE(1 << 4),
    COUNTER_ATTACK(1 << 5),
    DEFENSIBLE(1 << 6),
    TECHNIQUE(1 << 7),
    ART(1 << 8),
    WISTERIA(1 << 9);

    private final int value;
    AttackFlag(int value) {
      this.value = value;
    }
    public int getRawValue() {
      return this.value;
    }
  }

  public static AttackFlags of(int flags) {
    return new AttackFlags(flags);
  }

  public static final AttackFlag UNAVOIABLE = AttackFlag.UNAVOIABLE;
  public static final AttackFlag INDEFENSIBLE = AttackFlag.INDEFENSIBLE;
  public static final AttackFlag AREA = AttackFlag.AREA;
  public static final AttackFlag NOT_COUNTER_ATTACKABLE = AttackFlag.NOT_COUNTER_ATTACKABLE;
  public static final AttackFlag COUNTER_ATTACK = AttackFlag.COUNTER_ATTACK;
  public static final AttackFlag DEFENSIBLE = AttackFlag.DEFENSIBLE;
  public static final AttackFlag TECHNIQUE = AttackFlag.TECHNIQUE;
  public static final AttackFlag ART = AttackFlag.ART;
  public static final AttackFlag WISTERIA = AttackFlag.WISTERIA;

  private int flags = 0;
  public AttackFlags() {}
  public AttackFlags(AttackFlag initial) {
    this.flags = initial.getRawValue();
  }
  private AttackFlags(int ordinal) {
    this.flags = ordinal;
  }

  public void set(AttackFlag flag) {
    this.flags |= flag.getRawValue();
  }

  public boolean has(AttackFlag flag) {
    return (this.flags & flag.getRawValue()) != 0;
  }
}
