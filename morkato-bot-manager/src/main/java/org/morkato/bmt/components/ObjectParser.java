package org.morkato.bmt.components;

import org.morkato.bmt.registration.management.ObjectParserRegistryManagement;
import org.morkato.bmt.context.CommandContext;

public interface ObjectParser<T> {
  T parse(CommandContext<?> context,String text) throws Throwable;
  default void flush(ObjectParserRegistryManagement registries) throws Throwable {}
}
