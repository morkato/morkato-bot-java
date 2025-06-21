package org.morkato.bmt.startup.payload;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.startup.attributes.SlashCommandAttributes;

public record SlashCommandPayload<T>(CommandHandler<T> slashcommand, SlashCommandAttributes attrs) {
}
