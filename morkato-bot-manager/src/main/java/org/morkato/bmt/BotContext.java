package org.morkato.bmt;

import org.morkato.bmt.startup.builder.AppBuilder;

public interface BotContext {
  AppBuilder getAppCommandsTree();
}
