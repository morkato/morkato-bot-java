package org.morkato.api;

import org.morkato.api.entity.attack.AttackFlags;
import org.morkato.utility.mcisid.McisidUtil;

public class Main {
  public static void main(String[] args) {
//    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    Validator validator = factory.getValidator();
//    ArtDTO dto = new ArtDTO("", "", null, ArtType.RESPIRATION, null, null);
//    Set<ConstraintViolation<ArtDTO>> violations = dto.validate(validator);
//    System.out.println(Arrays.toString(violations.toArray()));
    String id = "0F2G0uLAemP0";
    System.out.println(McisidUtil.isIdModel(id, 15));
  }
}
