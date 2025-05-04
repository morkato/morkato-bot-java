package org.morkato.bmt.generated.registries;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.morkato.bmt.components.Command;
import org.morkato.bmt.registration.attributes.SlashCommandAttributes;

import java.util.Objects;

public class SlashCommandRegistry<T> {
  private final String name;
  private final String description;
  private final Command<T> slashcommand;
  private final SlashMapperRegistry<T> options;

  public SlashCommandRegistry(Command<T> slashcommand, SlashMapperRegistry<T> options, SlashCommandAttributes attrs) {
    this.slashcommand = Objects.requireNonNull(slashcommand);
    this.name = attrs.getName();
    this.description = attrs.getDescription();
    this.options = Objects.requireNonNull(options);
  }

  public void invoke(SlashCommandInteractionEvent event) throws Throwable {
    T result = options.mapInteraction(event);
    throw new RuntimeException("SlashCommand is not invokable.");
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

  public OptionData[] getOptions() {
    return options.createOptions().toArray(OptionData[]::new);
  }
}
