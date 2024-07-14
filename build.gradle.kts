import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform {
        includeEngines = setOf("junit-jupiter")
    }
}