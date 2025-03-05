plugins {
  id("java-library")
}

group = "org.morkato.bmt"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation("org.reflections:reflections:0.10.2")
  implementation(project(":morkato-bot-manager"))
  implementation(project(":morkato-utils"))
}

tasks.test {
  useJUnitPlatform()
}