import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("application")
  kotlin("jvm")
}

val morkhome: String = System.getenv("MORKATO_HOME") ?: ".."
val morkatoconf: String = "${morkhome}/morkato.conf"

group = "org.morkato.bot"
version = "1.0"

application.mainClass.set("org.morkato.bot.Client")
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
kotlin.jvmToolchain(21)
repositories.mavenCentral()

dependencies {
  testImplementation(kotlin("test"))
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation(project(":morkato-bot-manager:common"))
  implementation(project(":morkato-api-database"))
  implementation(project(":morkato-utils"))
  implementation(project(":morkato-api"))
}

tasks.named<JavaExec>("run") {
  jvmArgs = listOf(
    "-Xmx1024M", /* Máximo 1GB de RAM para consumo */
    "-Xms64M", /* Mínimo 64MB de RAM para consumo */
    "-Dmorkato.conf=${morkatoconf}" /* Arquivo de configuração */
  )
}

tasks.named<ShadowJar>("shadowJar") {
  archiveFileName.set("morkato-bot-$version.jar")
  destinationDirectory.set(file(morkhome))
  archiveClassifier.set("all")
  manifest.attributes.put("Main-Class", application.mainClass.get())
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  mergeServiceFiles()
  println("O build foi um sucesso. O jar está disponível em: ${destinationDirectory.get()}/${archiveFileName.get()}")
}

tasks.test {
  useJUnitPlatform()
}