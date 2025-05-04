package org.morkato.bmt.registration.payload;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.components.SlashCommand;
import org.morkato.bmt.registration.attributes.SlashCommandAttributes;

public record SlashCommandPayload<T>(Command<T> slashcommand,SlashCommandAttributes attrs) {
}
