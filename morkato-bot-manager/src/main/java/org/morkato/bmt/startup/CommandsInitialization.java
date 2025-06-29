package org.morkato.bmt.startup;

import org.morkato.bmt.components.ActionHandler;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bmt.generated.ActionsStaticRegistries;
import org.morkato.bmt.generated.ParsersStaticRegistries;
import org.morkato.bmt.generated.registries.ActionRegistry;
import org.morkato.bmt.generated.registries.ObjectParserRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CommandsInitialization {
  private final ParsersStaticRegistries parsers;
  private final ActionsStaticRegistries actions;

  public CommandsInitialization(ParsersStaticRegistries parsers, ActionsStaticRegistries actions) {
    this.parsers = parsers;
    this.actions = actions;
  }

  @SuppressWarnings("unchecked")
  public <T> ActionRegistry<T> action(Class<? extends ActionHandler<T>> actionclass) {
    return (ActionRegistry<T>)Objects.requireNonNull(actions.getAction(actionclass));
  }

  @SuppressWarnings("unchecked")
  public <T extends ObjectParser<?>> ObjectParserRegistry<T> objectParser(Class<T> objectParserClazz) {
    return (ObjectParserRegistry<T>)Objects.requireNonNull(parsers.getObjectParser(objectParserClazz));
  }
}
