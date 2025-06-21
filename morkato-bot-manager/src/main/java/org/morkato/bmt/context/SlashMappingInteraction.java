package org.morkato.bmt.context;

public interface SlashMappingInteraction {
  SlashOptionBuilder asString(String name);
  SlashOptionBuilder asLong(String name);
}
