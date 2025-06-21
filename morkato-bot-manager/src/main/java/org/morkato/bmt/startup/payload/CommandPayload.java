package org.morkato.bmt.startup.payload;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.startup.attributes.CommandAttributes;

public record CommandPayload<T>(CommandHandler<T> command, CommandAttributes attrs) {
}
