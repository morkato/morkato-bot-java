package org.morkato.bot.parsers;

import org.morkato.bmt.parser.RecordGenericParser;
import org.morkato.bot.parameters.ArtCommandData;
import org.morkato.bmt.annotation.Component;
import org.morkato.bmt.components.ObjectParser;

@Component
public class ArtCommandDataParser
  extends RecordGenericParser<ArtCommandData>
  implements ObjectParser<ArtCommandData> {
  public ArtCommandDataParser() throws NoSuchMethodException {
    super(ArtCommandData.class);
  }
}
