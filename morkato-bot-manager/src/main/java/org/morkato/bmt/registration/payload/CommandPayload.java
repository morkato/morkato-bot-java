package org.morkato.bmt.registration.payload;

import org.morkato.bmt.components.Command;
import org.morkato.bmt.registration.attributes.CommandAttributes;

public record CommandPayload<T>(Command<T> command, CommandAttributes attrs) {
}
