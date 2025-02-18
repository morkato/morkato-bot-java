plugins {
  java
  kotlin("jvm") version "2.1.0"
}

group = "com.morkato"
version = "1.0"

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

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("javax.annotation:javax.annotation-api:1.3.2")
  implementation("org.jetbrains:annotations:24.0.0")
  implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(23)
}