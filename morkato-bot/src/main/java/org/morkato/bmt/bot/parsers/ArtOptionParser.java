package org.morkato.bmt.bot.parsers;

import org.morkato.bmt.parser.EnumGenericParser;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.components.ObjectParser;
import org.morkato.bmt.bot.parameters.ArtOption;

@MorkatoComponent
public class ArtOptionParser
  extends EnumGenericParser<ArtOption>
  implements ObjectParser<ArtOption> {
  public ArtOptionParser() {
    super(ArtOption.class);
  }
}
