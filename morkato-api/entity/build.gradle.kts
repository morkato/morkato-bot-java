plugins {
  id("java")
  id("java-library")
}

group = "org.morkato.api.entity"
version = "1.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
repositories.mavenCentral()

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("com.google.code.findbugs:jsr305:3.0.2")
  api(project(":morkato-api"))
}

tasks.test {
    useJUnitPlatform()
}