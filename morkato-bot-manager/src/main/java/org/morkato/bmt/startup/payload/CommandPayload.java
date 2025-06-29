package org.morkato.bmt.startup.payload;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.startup.attributes.CommandAttributes;

public record CommandPayload<T>(Class<? extends CommandHandler<T>> command, CommandAttributes attrs) {
}
