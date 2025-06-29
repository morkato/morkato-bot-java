package org.morkato.bmt.startup.payload;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.startup.attributes.SlashCommandAttributes;

public record SlashCommandPayload<T>(Class<? extends CommandHandler<T>> slashcommand, SlashCommandAttributes attrs) {
}
