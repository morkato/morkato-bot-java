package org.morkato.mcbmt.startup.payload;

import org.morkato.mcbmt.components.ActionHandler;
import org.morkato.mcbmt.startup.attributes.ActionAttributes;

public record ActionPayload<T>(ActionHandler<T> actionhandler, ActionAttributes attrs) {

}
