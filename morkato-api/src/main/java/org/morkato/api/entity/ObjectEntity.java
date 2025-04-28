package org.morkato.api.entity;

import lombok.Getter;
import org.morkato.utility.mcisid.McisidUtil;

import java.util.Objects;

@Getter
public class ObjectEntity implements ObjectId {
  private final String id;

  public ObjectEntity(String id) {
    if (!McisidUtil.isValidId(id))
      throw new RuntimeException("ID is invalid!");
    this.id = Objects.requireNonNull(id);
  }
}
