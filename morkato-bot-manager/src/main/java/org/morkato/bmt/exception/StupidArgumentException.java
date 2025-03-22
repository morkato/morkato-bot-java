package org.morkato.bmt.exception;

public class StupidArgumentException extends RuntimeException {
  public StupidArgumentException() {
    super("You have not passed arguments.");
  }
}
