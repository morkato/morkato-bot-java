plugins {
  java
  kotlin("jvm") version "1.9.25"
}

group = "org.morkato"
version = "1.0"

allprojects {
  repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://jitpack.io")
    maven("https://m2.dv8tion.net/releases")
  }

  plugins.withType<JavaPlugin> {
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
  }
}

dependencies {
  implementation("javax.annotation:javax.annotation-api:1.3.2")
  implementation("org.jetbrains:annotations:24.0.0")
}

kotlin {
  jvmToolchain(21)
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed", "standardOut", "standardError")
    showStandardStreams = true
  }
}

tasks.register<JavaExec>("runClient") {
  group = "application"
  description = "Executa o cliente discord bot."
  getMainClass().set("org.morkato.bot.Client")
  classpath = sourceSets.main.get().getRuntimeClasspath()
  jvmArgs = listOf("-Xmx1024m", "-Dmorkato.conf=morkato.conf")
}

tasks.register("runHttpApi") {
  group = "application"
  description = "Executa a API HTTP para fornecimento do morkato-api"
  dependsOn(":morkato-http-api:bootRun")
}