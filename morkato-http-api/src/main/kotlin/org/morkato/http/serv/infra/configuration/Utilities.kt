package org.morkato.http.serv.infra.configuration

import org.morkato.utility.MorkatoMetadataExtractor
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean

@Configuration
class Utilities {
  val extractor = MorkatoMetadataExtractor()
  @Bean
  fun getMorkatoMetadataExtractor() : MorkatoMetadataExtractor {
    return extractor;
  }
}