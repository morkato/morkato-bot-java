plugins {
  kotlin("jvm")
}

group = "org.morkato.utility"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
  implementation("org.apache.commons:commons-text:1.10.0")
  implementation("com.google.guava:guava:33.4.0-jre")
}

tasks.test {
  useJUnitPlatform()
}