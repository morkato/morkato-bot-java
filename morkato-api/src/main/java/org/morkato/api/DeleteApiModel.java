package org.morkato.api;

import java.util.concurrent.Future;

public interface DeleteApiModel<T> {
  Future<T> delete();
}
