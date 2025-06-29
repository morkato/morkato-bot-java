package org.morkato.bmt.commands.rules;

public interface SlashMappingInteraction {
  SlashOptionBuilder asString(String name);
  SlashOptionBuilder asLong(String name);
}
