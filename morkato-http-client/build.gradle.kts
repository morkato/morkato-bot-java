plugins {
  id("java")
}

group = "org.morkato.client.http"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("com.google.code.findbugs:jsr305:3.0.2")
  implementation("org.apache.httpcomponents:httpasyncclient:4.1.5")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation(project(":morkato-utils"))
  implementation(project(":morkato-api"))
  compileOnly("org.projectlombok:lombok:0.11.0")
}

tasks.test {
  useJUnitPlatform()
}