project.setProperty("mainClassName", "MainKt")

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("io.ktor.plugin") version "3.0.3"

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("io.ktor:ktor-server-core:3.0.3")
    implementation("io.ktor:ktor-server-netty:3.0.3")
    implementation("io.ktor:ktor-server-cio:3.0.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")



}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(15)
}