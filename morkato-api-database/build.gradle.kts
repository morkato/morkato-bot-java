import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
}

group = "org.morkato.database"
version = "1.0"

repositories {
  mavenCentral()
}

tasks.test {
  useJUnitPlatform()
  systemProperties.set("morkato.conf", "../morkato.conf")
  testLogging {
    events("passed", "skipped", "failed", "standardOut", "standardError")
    showStandardStreams = true
  }
}

dependencies {
  testImplementation(kotlin("test"))
  testImplementation(project(":morkato-api-tests"))
  implementation("org.jetbrains.exposed:exposed-core:0.44.0")
  implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
  implementation("com.zaxxer:HikariCP:5.1.0")
  implementation("org.flywaydb:flyway-core")
  implementation("org.flywaydb:flyway-database-postgresql:10.0.0")
  implementation("org.postgresql:postgresql:42.7.5")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation(project(":morkato-utils"))
  implementation(project(":morkato-api"))
}