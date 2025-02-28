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
  implementation("com.google.guava:guava:33.4.0-jre")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
}

tasks.test {
  useJUnitPlatform()
}