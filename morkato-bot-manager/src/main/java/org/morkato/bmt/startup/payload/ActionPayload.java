package org.morkato.bmt.startup.payload;

import org.morkato.bmt.components.ActionHandler;
import org.morkato.bmt.startup.attributes.ActionAttributes;

public record ActionPayload<T>(ActionHandler<T> actionhandler, ActionAttributes attrs) {

}
