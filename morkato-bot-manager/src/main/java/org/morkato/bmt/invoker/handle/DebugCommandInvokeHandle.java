package org.morkato.bmt.invoker.handle;

import org.morkato.bmt.impl.TextCommandContextImpl;
import org.morkato.bmt.generated.registries.CommandRegistry;
import org.morkato.bmt.registration.MapRegistryManagement;
import org.morkato.bmt.components.CommandException;
import net.dv8tion.jda.api.entities.Message;
import org.morkato.utility.StringView;

import java.util.concurrent.TimeUnit;

public class DebugCommandInvokeHandle<T> extends CommandInvokeHandle<T> {
  public DebugCommandInvokeHandle(
    MapRegistryManagement<Class<? extends Throwable>,CommandException<?>> exceptions,
    CommandRegistry<T> registry,
    Message message,
    StringView view
  ) {
    super(exceptions, registry, message, view);
  }

  @Override
  public void run() {
    final String commandName = registry.getCommandClassName();
    long processTime = 0;
    long spawnContext = System.nanoTime();
    final TextCommandContextImpl<T> impl = registry.spawnContext(message);
    processTime += (System.nanoTime() - spawnContext);
    LOGGER.trace("Spawn of the context: {} for command: {} demanded: {}ms for processing.", impl, commandName, TimeUnit.NANOSECONDS.toMillis(processTime));
    try {
      long prepareContext = System.nanoTime();
      registry.prepareContext(impl, view);
      processTime += (System.nanoTime() - prepareContext);
      LOGGER.trace("Prepare context for command: {} demanded: {}ms", commandName, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - prepareContext));
      long processCommand = System.nanoTime();
      registry.invoke(impl);
      processTime += (System.nanoTime() - processCommand);
      LOGGER.trace("Process command invoker: {} demanded: {}ms", commandName, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - processCommand));
    } catch (Throwable exc) {
      LOGGER.debug("An unexpected error occurred in command: {}. Exception: {}.", impl.getCommand().getClass().getSimpleName(), exc.getClass().getName());
      long processException = System.nanoTime();
      this.onException(impl, exc);
      processTime += (System.nanoTime() - processException);
      LOGGER.trace("Exception: {} captured in command: {} demanded: {}ms for processing.", exc.getClass().getName(), commandName, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - processException));
    }
    LOGGER.debug("Process of the command: {} demanded: {}ms.", commandName, TimeUnit.NANOSECONDS.toMillis(processTime));
  }
}
