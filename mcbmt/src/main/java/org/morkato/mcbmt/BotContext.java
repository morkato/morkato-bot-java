package org.morkato.mcbmt;

import org.morkato.mcbmt.startup.builder.AppBuilder;

public interface BotContext {
  AppBuilder getAppCommandsTree();
}
