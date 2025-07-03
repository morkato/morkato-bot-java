package org.morkato.mcbmt;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.morkato.mcbmt.actions.ActionSessionManagement;
import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.actions.LayoutAction;
import org.morkato.mcbmt.generated.ActionsStaticRegistries;
import org.morkato.mcbmt.generated.CommandsStaticRegistries;
import org.morkato.mcbmt.generated.ExceptionsHandleStaticRegistries;
import org.morkato.mcbmt.generated.ParsersStaticRegistries;
import org.morkato.mcbmt.generated.registries.CommandExceptionRegistry;
import org.morkato.mcbmt.generated.registries.CommandRegistry;
import org.morkato.mcbmt.generated.registries.SlashCommandRegistry;
import org.morkato.mcbmt.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class BotCore implements Shutdownable {
  private static final Logger LOGGER = LoggerFactory.getLogger(BotCore.class);
  private Set<Invoker<?>> invokers = new HashSet<>();
  private final ActionSessionManagement sessions;
  private final ParsersStaticRegistries parsers;
  private final ExceptionsHandleStaticRegistries exceptions;
  private final ActionsStaticRegistries actions;
  private final CommandsStaticRegistries commands;

  public BotCore(
    ParsersStaticRegistries parsers,
    ExceptionsHandleStaticRegistries exceptions,
    ActionsStaticRegistries actions,
    CommandsStaticRegistries commands
  ) {
    this.sessions = new ActionSessionManagement();
    this.parsers = parsers;
    this.exceptions = exceptions;
    this.actions = actions;
    this.commands = commands;
  }

  public void invoke(Invoker<?> invoker) {
    try {
      invoker.start(this);
      invokers.add(invoker);
    } catch (Exception exc) {
      LOGGER.error("An unexpected error occurred while started invoker: {}. Ignoring...", invoker, exc);
    }
  }

  public void clear() {
  }

  @Override
  public void shutdown() {
    this.clear();
    for (Invoker<?> invoker : invokers)
      invoker.shutdown();
  }

  protected void syncSlashCommands(JDA jda) {
    LOGGER.info("Preparing to sync slashcommands with discord app.");
    CommandListUpdateAction action = jda.updateCommands();
    SlashCommandRegistry<?>[] slashcommands = commands.getRegisteredSlashCommands();
    for (SlashCommandRegistry<?> slash : slashcommands) {
      action = action.addCommands(
        Commands.slash(slash.getName(), slash.getDescription() == null ? "..." : slash.getDescription())
          .addOptions(slash.getOptions())
      );
    }
    action.queue();
  }

  public ParsersStaticRegistries getParsers() {
    return parsers;
  }

  public ExceptionsHandleStaticRegistries getExceptions() {
    return exceptions;
  }

  public ActionsStaticRegistries getActions() {
    return actions;
  }

  public CommandsStaticRegistries getCommands() {
    return commands;
  }

  public ActionSession<?> getActionSessionBySlot(int slot) {
    return sessions.getSessionBySlot(slot);
  }

  public CommandRegistry<?> getTextCommand(String commandname) {
    return commands.getTextCommand(commandname);
  }

  public SlashCommandRegistry<?> getSlashCommand(String name) {
    return commands.getSlashCommand(name);
  }

  public CommandExceptionRegistry<?> getCommandExceptionHandler(Class<?> clazz) {
    return exceptions.getCommandExceptionHandler(clazz);
  }

  public <T> ActionSession<T> createSession(
    LayoutAction layout,
    ActionHandler<T> actionhandler,
    User author,
    T payload
  ) {
    return sessions.createSession(
      layout,
      actionhandler,
      author,
      payload
    );
  }

  public void setSessionMessage(int slot, Message message) {
    sessions.setMessageSessionSlot(slot, message);
  }

  public int occupiedNextSlot(int offset) {
    return sessions.occupiedNextSlot(offset);
  }

  public void flushSession(ActionSession<?> session) {
    sessions.flushSession(session);
  }

  public void lockSessionSlot(int slot) {
    sessions.lockSlot(slot);
  }

  public void unlockSessionSlot(int slot) {
    sessions.unlockSlot(slot);
  }

  public boolean isSessionSlotLocked(int slot) {
    return sessions.isLockedSlot(slot);
  }
}
