package org.morkato.mcbmt.startup;

import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.components.ObjectParser;
import org.morkato.mcbmt.generated.ActionsStaticRegistries;
import org.morkato.mcbmt.generated.ParsersStaticRegistries;
import org.morkato.mcbmt.generated.registries.ActionRegistry;
import org.morkato.mcbmt.generated.registries.ObjectParserRegistry;

import java.util.Objects;

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
