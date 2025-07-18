package org.morkato.mcbmt;

import org.morkato.mcbmt.generated.registries.ObjectParserRegistry;
import org.morkato.mcbmt.generated.registries.SlashMapperRegistry;
import org.morkato.mcbmt.components.ObjectParser;
import org.morkato.mcbmt.components.SlashMapper;
import org.morkato.mcbmt.commands.rules.SlashMappingInteraction;
import org.morkato.mcbmt.commands.rules.SlashMapperData;
import org.morkato.mcbmt.commands.CommandContext;

public interface NoArgs {
  static final NoArgs INSTANCE = new NoArgsInstance();
  static final ObjectParser<NoArgs> PARSER = new NoArgsObjetParser();
  static final SlashMapper<NoArgs> SLASH_MAPPER = new NoArgsSlashMapper();
  static final ObjectParserRegistry<NoArgs> PARSER_REGISTRY = new ObjectParserRegistry<>(PARSER, NoArgs.class);
  static final SlashMapperRegistry<NoArgs> SLASH_MAPPER_REGISTRY = new SlashMapperRegistry<>(SLASH_MAPPER);
  class NoArgsInstance implements NoArgs { }
  class NoArgsObjetParser implements ObjectParser<NoArgs> {
    @Override
    public NoArgs parse(CommandContext<?> context, String text) {
      return INSTANCE;
    }
  }
  class NoArgsSlashMapper implements SlashMapper<NoArgs> {
    @Override
    public void createOptions(SlashMappingInteraction interaction) {}
    @Override
    public NoArgs mapInteraction(SlashMapperData payload) {
      return INSTANCE;
    }
  }

  static boolean isNoArgs(Class<?> clazz) {
    return clazz == NoArgs.class;
  }

  static boolean isInstance(Object obj) {
    return obj == INSTANCE;
  }

  static boolean isNoArgsSlashMapper(Object obj) {
    return obj == SLASH_MAPPER || obj == SLASH_MAPPER_REGISTRY;
  }
}
