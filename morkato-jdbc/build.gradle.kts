plugins {
  id("java")
  id("java-library")
}

group = "org.morkato.jdbc"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("ch.qos.logback:logback-classic:1.5.13")
  api("org.springframework:spring-jdbc:6.2.6")
}

tasks.test {
  useJUnitPlatform()
}