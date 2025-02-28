plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "morkato-bot-kts"
include("morkato-bot")
include("morkato-bot-manager")
include("morkato-utils")
//include("morkato-http-client")
//include("morkato-http-api")
include("morkato-api-database")
include("morkato-api")
include("morkato-api-tests")
