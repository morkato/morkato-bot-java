plugins {
  id("java")
  id("java-library")
}

group = "org.morkato.api"
version = "1.0"

repositories.mavenCentral()

dependencies {
  implementation(platform("org.junit:junit-bom:5.10.0"))
  implementation("org.junit.jupiter:junit-jupiter")
  api(project(":morkato-api"))
}

tasks.test {
  useJUnitPlatform()
}