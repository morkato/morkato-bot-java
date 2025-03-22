plugins {
  kotlin("jvm")
  id("java")
  id("java-library")
}

group = "org.morkato.bmt"
version = "1.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
kotlin.jvmToolchain(21)
repositories.mavenCentral()

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("com.google.code.findbugs:jsr305:3.0.2")
  implementation("org.reflections:reflections:0.10.2")
  implementation(project(":morkato-utils"))
  api(project(":morkato-bot-manager"))
}

tasks.test {
  useJUnitPlatform()
}