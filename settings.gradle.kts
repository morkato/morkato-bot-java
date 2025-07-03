plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "morkato-bot-kts"
include("mcbmt:common")
include("mcbmt")
include("morkato-bot")
include("morkato-boot")
include("mcisid-java")
