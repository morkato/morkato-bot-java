package org.morkato.mcbmt.actions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.morkato.mcbmt.components.ActionHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.BitSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ActionSessionManagement {
  private static final Logger LOGGER = LoggerFactory.getLogger(ActionSessionManagement.class);
  private static final int MAX_SESSIONS_SLOTS = 4096;
  private final ActionSession<?>[] sessions = new ActionSession[MAX_SESSIONS_SLOTS];
  private final BitSet availableSessions = new BitSet(MAX_SESSIONS_SLOTS);
  private final BitSet lockedSlots = new BitSet(MAX_SESSIONS_SLOTS);
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock read = lock.readLock();
  private final Lock write = lock.writeLock();

  public boolean isSlotOccupied(int slot) {
    if (slot < 0 || slot >= MAX_SESSIONS_SLOTS)
      /* TODO: Adicionar um erro mais bacana. */
      throw new RuntimeException("");
    return availableSessions.get(slot);
  }

  public ActionSession<?> getSessionBySlot(int slot) {
    read.lock();
    try {
      if (!this.isSlotOccupied(slot))
        return null;
      return sessions[slot];
    } finally {
      read.unlock();
    }
  }

  public void lockSlot(int slot) {
    write.lock();
    if (slot < 0 || slot >= MAX_SESSIONS_SLOTS)
      /* TODO: Adicionar um erro mais bacana. */
      throw new RuntimeException("");
    lockedSlots.set(slot);
    write.unlock();
  }

  public void unlockSlot(int slot) {
    write.lock();
    if (slot < 0 || slot >= MAX_SESSIONS_SLOTS)
      /* TODO: Adicionar um erro mais bacana. */
      throw new RuntimeException("");
    lockedSlots.clear(slot);
    write.unlock();
  }

  public boolean isLockedSlot(int slot) {
    if (slot < 0 || slot >= MAX_SESSIONS_SLOTS)
      /* TODO: Adicionar um erro mais bacana. */
      throw new RuntimeException("");
    return lockedSlots.get(slot);
  }

  public <T> ActionSession<T> createSession(
    LayoutAction layout,
    ActionHandler<T> actionhandler,
    User author,
    T payload
  ) {
    write.lock();
    try {
      int slot = availableSessions.nextClearBit(0);
      availableSessions.set(slot);
      final ActionSession<T> session = new ActionSessionImpl<>(
        this,
        layout,
        actionhandler,
        author,
        payload,
        System.currentTimeMillis() + (long)(1000 * 5),
        slot
      );
      sessions[slot] = session;
      LOGGER.debug("Session for user: {} (id={}) has created into the slot: {}",
        author.getGlobalName(), author.getId(), slot);
      return session;
    } finally {
      write.unlock();
    }
  }

  public void setMessageSessionSlot(int slot, Message message) {
    write.lock();
    if (!this.isSlotOccupied(slot))
      return;
    ((ActionSessionImpl<?>)sessions[slot]).setMessage(message);
    LOGGER.debug("Set message id: {} for session in slot: {}", message.getId(), slot);
    write.unlock();
  }

  public void flushSession(ActionSession<?> session) {
    if (Objects.isNull(session))
      return;
    this.flushSession(session.getSlot());
  }

  public void flushSession(int slot) {
    if (!this.isSlotOccupied(slot))
      return;
    write.lock();
    sessions[slot] = null;
    availableSessions.clear(slot);
    lockedSlots.clear(slot);
    write.unlock();
  }

  public int occupiedNextSlot(int offset) {
    return availableSessions.nextSetBit(offset);
  }
}
