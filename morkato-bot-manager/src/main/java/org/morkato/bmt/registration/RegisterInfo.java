package org.morkato.bmt.registration;

import java.util.Collection;

public interface RegisterInfo<T> {
  Collection<T> registries();
  int size();
}
