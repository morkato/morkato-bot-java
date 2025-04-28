plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "morkato-bot-kts"
include("morkato-bot-manager:common")
include("morkato-bot-manager")
include("morkato-bot")
include("morkato-api:database")
include("morkato-api:tests")
include("morkato-api")
include("morkato-utils")
include("morkato-api:entity")
include("morkato-jdbc")
include("morkato-boot")
