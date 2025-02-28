package org.morkato.http;

import javax.annotation.Nonnull;

public enum RequestMethod {
  GET("GET"),
  POST("POST"),
  PUT("PUT"),
  DELETE("DELETE");
  private final String value;
  private RequestMethod(String value) {
    this.value = value;
  }
  @Nonnull
  public String getRaValue() {
    return this.value;
  }
}
