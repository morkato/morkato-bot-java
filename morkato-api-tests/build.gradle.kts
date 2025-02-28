plugins {
  id("java")
}

group = "org.morkato.api"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("org.junit.jupiter:junit-jupiter:5.9.1")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("io.mockk:mockk:1.13.2")
  implementation(project(":morkato-api"))
}

tasks.test {
  useJUnitPlatform()
}