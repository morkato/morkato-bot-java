plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "morkato-bot-kts"
include("morkato-bot")
include("morkato-resolver")
include("morkato-bot-manager")
include("morkato-utils")
include("morkato-service")
