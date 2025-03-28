package org.morkato.api.exception.art;

import org.morkato.api.entity.art.ArtId;

public class ArtNotFoundException extends RuntimeException{
  public ArtNotFoundException(ArtId query){
    super("");
  }
}
