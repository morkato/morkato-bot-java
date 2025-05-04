package org.morkato.bmt.registration.payload;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.registration.attributes.SlashCommandAttributes;

public record SlashCommandPayload<T>(CommandHandler<T> slashcommand,SlashCommandAttributes attrs) {
}
