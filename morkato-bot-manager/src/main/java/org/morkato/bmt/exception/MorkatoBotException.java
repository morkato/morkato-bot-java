package org.morkato.bmt.exception;

import org.jetbrains.annotations.NotNull;

public class MorkatoBotException extends Exception {
  public  MorkatoBotException(
    @NotNull String message
  ) {
    super(message);
  }
}
