plugins {
  application
  kotlin("jvm")
}

group = "org.morkato.bot"
version = "1.0"

application {
  mainClass.set("com.morkato.bot.Client")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation("net.dv8tion:JDA:5.0.0-beta.18")
  implementation("org.jetbrains.exposed:exposed-core:0.44.0")
  implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("org.flywaydb:flyway-core")
  implementation("org.flywaydb:flyway-database-postgresql:10.0.0")
  implementation(project(":morkato-bot-manager"))
  implementation(project(":morkato-api-database"))
  implementation(project(":morkato-utils"))
  implementation(project(":morkato-api"))
}



tasks.register<JavaExec>("runClient") {
  group = "application"
  description = "Executa o cliente discord bot."
  getMainClass().set("org.morkato.bot.Client")
  classpath = sourceSets.main.get().getRuntimeClasspath()
  jvmArgs = listOf("-Xmx1024m", "-Dmorkato.conf=../morkato.conf")
}

tasks.register<Jar>("clientJar") {
  dependsOn(":morkato-bot-manager:bmtJar")
  dependsOn("build")
  group = "build"
  description = "Cria um Jar para o discord bot."
  getArchiveFileName().set("morkato-bot-$version.jar")
  getDestinationDirectory().set(file("."))
  manifest {
    attributes["Main-Class"] = "com.morkato.bot.Client"
    attributes["Class-Path"] = sourceSets.main.get().getRuntimeClasspath()
  }
  from(sourceSets["main"].output)
  from(configurations.runtimeClasspath.get())
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}