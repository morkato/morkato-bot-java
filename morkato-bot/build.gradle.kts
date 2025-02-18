plugins {
  application
  kotlin("jvm")
}

group = "com.morkato.bot"
version = "1.0"

application {
  mainClass.set("com.morkato.bot.Client")
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation("org.springframework:spring-context:6.0.0")
  implementation("net.dv8tion:JDA:5.0.0-beta.18")
  implementation(project(":morkato-bot-manager"))
}



tasks.register<JavaExec>("runClient") {
  group = "application"
  description = "Executa o cliente discord bot."
  getMainClass().set("com.morkato.bot.Client")
  classpath = sourceSets.main.get().getRuntimeClasspath()
  jvmArgs = listOf("-Xmx1024m")
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
  jvmToolchain(23)
}