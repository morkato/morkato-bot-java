plugins {
  kotlin("jvm")
}

group = "com.morkato.service"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.postgresql:postgresql:42.7.1")
  implementation("org.flywaydb:flyway-core:9.22.3")
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  implementation("org.jooq:jooq:3.17.6")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  runtimeOnly("org.flywaydb:flyway-database-postgresql")
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(23)
}