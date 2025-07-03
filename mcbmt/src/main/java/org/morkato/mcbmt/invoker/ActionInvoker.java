package org.morkato.mcbmt.invoker;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.morkato.mcbmt.actions.ButtonAction;
import org.morkato.mcbmt.actions.ActionContext;
import org.morkato.mcbmt.actions.LayoutAction;
import org.morkato.mcbmt.generated.ActionsStaticRegistries;
import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.actions.ActionSession;
import org.morkato.mcbmt.BotCore;
import org.morkato.mcbmt.actions.ButtonClickedActionContext;
import org.morkato.mcbmt.util.StringView;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ActionInvoker implements Invoker<ButtonInteractionEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ActionInvoker.class);
  private BotCore core;
  private boolean ready = false;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "morkato-session-clean"));
  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private final Lock read = readWriteLock.readLock();
  private final Lock write = readWriteLock.writeLock();

  @Override
  public void start(BotCore core) {
    Objects.requireNonNull(core);
    scheduler.scheduleAtFixedRate(this::cleanActionSessions, 0, 5, TimeUnit.SECONDS);
    this.core = core;
    this.ready = true;
  }

  @Override
  public boolean isReady() {
    return ready;
  }

  @Override
  public void invoke(ButtonInteractionEvent interaction) {
    if (!ready)
      return;
    final String component = interaction.getComponentId();
    final StringView view = new StringView(component);
    final char prefix = view.next();
    if (prefix == '&')
      this.invokeStaticAction(interaction, view);
    else if (prefix == '-')
      this.invokeActionSession(interaction, view);
  }

  @Override
  public void shutdown() {
    LOGGER.info("Shutdown ActionInvoker. Closing all services.");
    scheduler.shutdown();
    try {
      if (scheduler.awaitTermination(10, TimeUnit.SECONDS))
        scheduler.shutdownNow();
    } catch (InterruptedException exc) {
      scheduler.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  @SuppressWarnings("unchecked")
  private void cleanActionSessions() {
    if (!ready)
      return;
    write.lock();
    try {
      for (int slot = core.occupiedNextSlot(0); slot >= 0; slot = core.occupiedNextSlot(slot  + 1)) {
        ActionSession<Object> session = (ActionSession<Object>)core.getActionSessionBySlot(slot);
        if (core.isSessionSlotLocked(session.getSlot()) || !session.isExpired())
          continue;
        session.getActionHandler().onSessionExpired(session);
        core.flushSession(session);
        LOGGER.debug("Session in slot: {} is expired. Dropped session.", slot);
      }
    } finally {
      write.unlock();
    }
  }

  private void invokeStaticAction(ButtonInteractionEvent interaction, StringView view) {
    final ActionsStaticRegistries actions = core.getActions();
    final String component = view.rest();
    final String[] components = component.split(":", 2);
    if (components.length != 2)
      /* TODO: Add a custom exception */
      throw new RuntimeException("");
    final String actionid = components[0];
    final String subcomponent = components[1];
    final ActionHandler<?> handler = actions.getActionById(actionid);
    /* Excluir */
  }

  private boolean isValidSession(ButtonInteractionEvent interaction, ActionSession<?> session) {
    return !session.isExpired()
      && Objects.nonNull(session.getMessage())
      && session.getAuthor().getIdLong() == interaction.getUser().getIdLong()
      && session.getMessage().getIdLong() == interaction.getMessageIdLong();
  }

  @SuppressWarnings("unchecked")
  private <T> void invokeActionSession(ButtonInteractionEvent interaction, StringView view) {
    long initial = System.nanoTime();
    final String sessionid = view.read(2);
    final int slot = LayoutAction.getUnpackedSlots(sessionid);
    ActionContext<T> context;
    ActionHandler<T> handler;
    read.lock();
    try {
      final ActionSession<T> session = (ActionSession<T>)core.getActionSessionBySlot(slot);
      if (Objects.isNull(session) || !this.isValidSession(interaction, session))
        return;
      final String component = view.rest();
      final LayoutAction layout = session.getLayout();
      final ButtonAction button = layout.getButtonReference(component);
      handler = session.getActionHandler();
      context = new ButtonClickedActionContext<>(interaction, session, button, component);
      core.lockSessionSlot(slot);
    } finally {
      read.unlock();
    }
    this.handleAction(handler, context, slot);
  }

  private <T> void handleAction(ActionHandler<T> action, ActionContext<T> ctx, int slot) {
    try {
      action.handleAction(ctx);
    } catch (Exception exc) {
      /* TODO: Algum despacho de erros */
    } finally {
      core.unlockSessionSlot(slot);
    }
  }
}
