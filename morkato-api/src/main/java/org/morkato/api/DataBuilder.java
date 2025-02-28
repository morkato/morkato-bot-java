package org.morkato.api;

import java.util.concurrent.Future;

public interface DataBuilder<T> {
  Future<T> queue();
}
