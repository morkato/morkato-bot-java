plugins {
  id("java")
}

group = "org.morkato"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("ch.qos.logback:logback-classic:1.5.13")
  implementation("com.google.code.findbugs:jsr305:3.0.2")
}

tasks.test {
  useJUnitPlatform()
}