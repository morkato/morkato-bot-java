package org.morkato.bot.parsers;

import org.morkato.bmt.parser.EnumGenericParser;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.components.ObjectParser;
import org.morkato.bot.parameters.ArtOption;

@Component
public class ArtOptionParser
  extends EnumGenericParser<ArtOption>
  implements ObjectParser<ArtOption> {
  public ArtOptionParser() {
    super(ArtOption.class);
  }
}
