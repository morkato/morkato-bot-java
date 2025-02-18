plugins {
  kotlin("jvm")
}

group = "com.morkato.utility"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("com.google.guava:guava:33.4.0-jre")
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(23)
}