package org.morkato.bmt.context;

import org.morkato.bmt.registration.AppCommandTree;

public interface BotContext {
  AppCommandTree getAppCommandsTree();
}
