package org.morkato.utility.exception;

public class NotSpecifiedFileConf extends MorkatoUtilityException {
  public final String property;
  public NotSpecifiedFileConf(String property) {
    super("Property: " + property + " is not specified");
    this.property = property;
  }
}
