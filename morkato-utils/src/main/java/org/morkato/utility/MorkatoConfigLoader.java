package org.morkato.utility;

import org.morkato.utility.exception.NotSpecifiedFileConf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MorkatoConfigLoader {
  private static final String DEFAULT_PROPERTY = "morkato.conf";
  public static Properties load(String sysProperty) throws FileNotFoundException, NotSpecifiedFileConf, IOException {
    Properties properties = new Properties();
    String fileconf = System.getProperty(sysProperty);
    if (fileconf == null)
      throw new NotSpecifiedFileConf(sysProperty);
    InputStream stream = new FileInputStream(fileconf);
    properties.load(stream);
    stream.close();
    return properties;
  }
  public static Properties loadDefault() throws FileNotFoundException, NotSpecifiedFileConf, IOException {
    return load(DEFAULT_PROPERTY);
  }
}
