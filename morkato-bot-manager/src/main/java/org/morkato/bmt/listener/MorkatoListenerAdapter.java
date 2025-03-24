package org.morkato.bmt.listener;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.morkato.bmt.registration.MorkatoBotManagerRegistration;

import javax.annotation.Nonnull;
import java.util.Objects;

public class MorkatoListenerAdapter extends ListenerAdapter {
  protected final MorkatoBotManagerRegistration registration;
  public MorkatoListenerAdapter(
    @Nonnull MorkatoBotManagerRegistration registration
  ) {
    Objects.requireNonNull(registration);
    this.registration = registration;
  }
}
