plugins {
  id("java-library")
  kotlin("jvm")
}

group = "org.morkato.database"
version = "1.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
repositories.mavenCentral()

dependencies {
  testImplementation(project(":morkato-api:tests"))
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("org.postgresql:postgresql:42.7.5")
  implementation("com.zaxxer:HikariCP:5.1.0")
  implementation("ch.qos.logback:logback-classic:1.5.13")
  implementation(project(":morkato-utils"))
  api("org.flywaydb:flyway-database-postgresql:10.0.0")
  api("org.flywaydb:flyway-core")
  api("org.jetbrains.exposed:exposed-core:0.44.0")
  api("org.jetbrains.exposed:exposed-jdbc:0.44.0")
  api("org.jetbrains.exposed:exposed-dao:0.44.0")
  api(project(":morkato-jdbc"))
  api(project(":morkato-api"))
}

tasks.test {
  useJUnitPlatform()
}