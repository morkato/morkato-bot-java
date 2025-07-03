package org.morkato.mcbmt.exception;

public class StupidArgumentException extends RuntimeException {
  public StupidArgumentException() {
    super("You have not passed arguments.");
  }
}
