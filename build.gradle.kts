plugins {
  id("java")
  kotlin("jvm") version "1.9.25"
}

group = "org.morkato"
version = rootProject.version

subprojects {
  layout.buildDirectory.set(file("${rootProject.projectDir}/build/gradle/${project.name}"))
}

layout.buildDirectory.set(file("${rootProject.projectDir}/build/gradle/root"))

allprojects {
  repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://jitpack.io")
    maven("https://m2.dv8tion.net/releases")
  }
}