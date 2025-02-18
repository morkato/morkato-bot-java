plugins {
  kotlin("jvm")
}

group = "com.morkato.bmt"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.springframework:spring-context:6.0.0")
  implementation("org.apache.commons:commons-lang3:3.12.0")
  implementation("com.google.code.findbugs:jsr305:3.0.2")
  implementation("ch.qos.logback:logback-classic:1.4.14")
  implementation("org.jetbrains:annotations:24.0.0")
  implementation("net.dv8tion:JDA:5.0.0-beta.18")
  implementation(project(":morkato-utils"))
}

tasks.register<Jar>("bmtJar") {
  dependsOn("build")
  group = "build"
  description = "Cria um Jar para o discord bot."
  getArchiveFileName().set("morkato-bmt-$version.jar")
  getDestinationDirectory().set(file("."))
  manifest {
    attributes["Main-Class"] = "com.morkato.bmt.Main"
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