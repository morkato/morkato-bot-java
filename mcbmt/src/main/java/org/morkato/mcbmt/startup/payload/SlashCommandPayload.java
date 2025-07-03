package org.morkato.mcbmt.startup.payload;

import org.morkato.mcbmt.components.CommandHandler;
import org.morkato.mcbmt.startup.attributes.SlashCommandAttributes;

public record SlashCommandPayload<T>(Class<? extends CommandHandler<T>> slashcommand, SlashCommandAttributes attrs) {
}
