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
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation(project(":morkato-bot-manager-starter"))
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

tasks.register<Jar>("buildClient") {
  dependsOn("build")
  group = "build"
  description = "Cria um Jar para o discord bot."
  getArchiveFileName().set("morkato-bot-$version.jar")
  getDestinationDirectory().set(file("."))
  archiveClassifier = "all"
  manifest {
    attributes["Main-Class"] = "org.morkato.bot.Client"
  }
  val dependencies = configurations
    .runtimeClasspath
    .get()
    .map(::zipTree)
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  from(dependencies)
  from(sourceSets["main"].output)
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}