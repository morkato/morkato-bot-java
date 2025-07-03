package org.morkato.mcbmt.generated.registries;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.morkato.mcbmt.components.CommandHandler;
import org.morkato.mcbmt.BotCore;
import org.morkato.mcbmt.commands.SlashCommandContext;
import org.morkato.mcbmt.commands.rules.SlashMapperDataImpl;
import org.morkato.mcbmt.commands.rules.SlashMappingInteractionImpl;
import org.morkato.mcbmt.startup.attributes.SlashCommandAttributes;

import java.util.Objects;

public class SlashCommandRegistry<T> {
  private final String name;
  private final String description;
  private final int flags;
  private final CommandHandler<T> slashcommand;
  private final SlashMapperRegistry<T> options;

  public SlashCommandRegistry(CommandHandler<T> slashcommand, SlashMapperRegistry<T> options, SlashCommandAttributes attrs) {
    this.slashcommand = Objects.requireNonNull(slashcommand);
    this.name = attrs.getName();
    this.description = attrs.getDescription();
    this.flags = attrs.getFlags();
    this.options = options;
  }

  public SlashCommandContext<T> bindContext(BotCore core, SlashCommandInteractionEvent event) {
    final T result = Objects.isNull(options) ? null : options.mapInteraction(new SlashMapperDataImpl(event));
    if (this.isDeferReply())
      event.deferReply(this.isResponseEphemeral()).queue();
    return new SlashCommandContext<>(
      core, event, this, result);
  }

  public void invoke(SlashCommandContext<T> ctx) throws Exception {
    slashcommand.invoke(ctx);
  }

  public String getCommandClassName() {
    return slashcommand.getClass().getName();
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isDeferReply() {
    return (flags & (1 << 1)) != 0;
  }

  public boolean isResponseEphemeral() {
    return (flags & (1 << 2)) != 0;
  }

  public OptionData[] getOptions() {
    if (Objects.isNull(options))
      return new OptionData[0];
    final SlashMappingInteractionImpl slashmapping = new SlashMappingInteractionImpl();
    options.createOptions(slashmapping);
    return slashmapping.build();
  }

  public CommandHandler<T> getCommandHandler() {
    return slashcommand;
  }
}
