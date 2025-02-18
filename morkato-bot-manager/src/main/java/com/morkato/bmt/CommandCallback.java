package com.morkato.bmt;

import com.morkato.bmt.commands.Context;

public interface CommandCallback {
  void accept(Context context) throws Throwable;
}
