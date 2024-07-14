plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.6")
}
