package org.morkato.bmt.bot.parsers;

import org.morkato.bmt.parser.RecordGenericParser;
import org.morkato.bmt.bot.parameters.ArtCommandData;
import org.morkato.bmt.annotation.MorkatoComponent;
import org.morkato.bmt.bmt.components.ObjectParser;

@MorkatoComponent
public class ArtCommandDataParser
  extends RecordGenericParser<ArtCommandData>
  implements ObjectParser<ArtCommandData> {
  public ArtCommandDataParser() throws NoSuchMethodException {
    super(ArtCommandData.class);
  }
}
