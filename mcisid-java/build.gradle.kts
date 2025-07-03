import java.io.ByteArrayOutputStream

plugins {
  id("java")
}

val jniHeadersDir = layout.buildDirectory.dir("jni-headers")

group = "org.morkato.mcisid"
version = "1.0"

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
}