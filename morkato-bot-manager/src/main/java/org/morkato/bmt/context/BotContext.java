package org.morkato.bmt.context;

import org.morkato.bmt.startup.AppCommandTree;

public interface BotContext {
  AppCommandTree getAppCommandsTree();
}
