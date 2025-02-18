package com.morkato.bmt.annotation;

public interface ParameterParser<T> {
  T parse(String text);
}
