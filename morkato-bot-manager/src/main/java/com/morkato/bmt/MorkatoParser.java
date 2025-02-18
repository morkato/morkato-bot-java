package com.morkato.bmt;

import com.morkato.bmt.commands.Context;

public interface MorkatoParser<T> {
  T parse(String query);
}
