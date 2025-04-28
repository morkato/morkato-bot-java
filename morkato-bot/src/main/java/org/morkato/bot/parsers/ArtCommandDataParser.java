package org.morkato.bot.parsers;

import org.morkato.bmt.parser.RecordGenericParser;
import org.morkato.bot.parameters.ArtCommandData;
import org.morkato.bmt.components.ObjectParser;

public class ArtCommandDataParser
  extends RecordGenericParser<ArtCommandData>
  implements ObjectParser<ArtCommandData> {
  public ArtCommandDataParser() throws NoSuchMethodException {
    super(ArtCommandData.class);
  }
}