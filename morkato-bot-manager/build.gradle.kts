plugins {
  id("java-library")
  kotlin("jvm")
}

group = "org.morkato.bmt"
version = "1.0"

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.apache.commons:commons-lang3:3.12.0")
  implementation("com.google.code.findbugs:jsr305:3.0.2")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation("org.jetbrains:annotations:24.0.0")
  implementation(project(":morkato-utils"))
  api("net.dv8tion:JDA:5.3.0")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(21)
}