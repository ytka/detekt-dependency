plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("maven-publish")
    //id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "io.github.ytka"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.6")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    //detektPlugins(files("libs/detekt-rules-package-restriction-$version.jar"))
}

tasks.test {
    useJUnitPlatform {
        includeEngines = setOf("junit-jupiter")
    }
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ytka/detekt-rules-package-restriction")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

/*
detekt {
    parallel = false
    config.setFrom(files("$rootDir/detekt.yml"))
}
*/