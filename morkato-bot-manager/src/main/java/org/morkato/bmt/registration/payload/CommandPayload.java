package org.morkato.bmt.registration.payload;

import org.morkato.bmt.components.CommandHandler;
import org.morkato.bmt.registration.attributes.CommandAttributes;

public record CommandPayload<T>(CommandHandler<T> command,CommandAttributes attrs) {
}
