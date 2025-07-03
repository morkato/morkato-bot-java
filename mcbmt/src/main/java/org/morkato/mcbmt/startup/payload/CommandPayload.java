package org.morkato.mcbmt.startup.payload;

import org.morkato.mcbmt.components.CommandHandler;
import org.morkato.mcbmt.startup.attributes.CommandAttributes;

public record CommandPayload<T>(Class<? extends CommandHandler<T>> command, CommandAttributes attrs) {
}
