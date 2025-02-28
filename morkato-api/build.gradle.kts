plugins {
  id("java")
}

group = "org.morkato.api"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.google.code.findbugs:jsr305:3.0.2")
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  implementation("org.projectlombok:lombok:1.18.30")
  implementation("org.apache.bval:bval-jsr:3.0.0")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
  useJUnitPlatform()
}