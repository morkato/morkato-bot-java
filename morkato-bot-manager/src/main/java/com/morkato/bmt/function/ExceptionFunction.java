package com.morkato.bmt.function;

import com.morkato.bmt.commands.Context;

public interface ExceptionFunction<T> {
  void accept(Context<?> context, T exception);
}
