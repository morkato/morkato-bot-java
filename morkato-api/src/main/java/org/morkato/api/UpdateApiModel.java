package org.morkato.api;

public interface UpdateApiModel<M, T extends DataBuilder<M>> {
  T doUpdate();
}
