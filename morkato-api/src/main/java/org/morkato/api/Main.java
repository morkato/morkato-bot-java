package org.morkato.api;

import org.morkato.api.entity.attack.AttackFlags;

public class Main {
  public static void main(String[] args) {
//    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    Validator validator = factory.getValidator();
//    ArtDTO dto = new ArtDTO("", "", null, ArtType.RESPIRATION, null, null);
//    Set<ConstraintViolation<ArtDTO>> violations = dto.validate(validator);
//    System.out.println(Arrays.toString(violations.toArray()));
    AttackFlags flags = new AttackFlags();
    System.out.println(flags.has(AttackFlags.INDEFENSIBLE));
    flags.set(AttackFlags.UNAVOIABLE);
    System.out.println(flags.has(AttackFlags.UNAVOIABLE));
  }
}
