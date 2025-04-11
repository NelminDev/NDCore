import com.vanniktech.maven.publish.SonatypeHost
import java.util.Calendar

/**
 * Configures the plugins used in the project.
 * - Applies the Kotlin JVM plugin for building Kotlin JVM targets.
 * - Adds the Kotlin Serialization plugin for working with serialization.
 * - Integrates the Shadow plugin for creating a shaded JAR file.
 * - Uses the Run Paper plugin for running a Minecraft Paper server.
 * - Configures Maven Publishing for publishing the artifact.
 * - Enables signing of published artifacts.
 * - Uses the Vanniktech Maven Publish Gradle Plugin for simplifying publication.
 */

/**
 *
 */

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("maven-publish")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.31.0"
}

group = project.property("project.group").toString()
version = project.property("project.version").toString()

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    // Ktor Client
    implementation("io.ktor:ktor-client-core:3.1.2")
    implementation("io.ktor:ktor-client-cio:3.1.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // Nelmins APIs
    api("dev.nelmin:lumina:1.0.3")
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }

    shadowJar {
        archiveClassifier.set("Spigot")
        archiveVersion.set("")
        dependencies {
            exclude(dependency("io.papermc.paper:paper-api:.*"))
        }
    }

    jar {
        archiveClassifier.set("")
    }
}

signing {
    useGpgCmd()
}

mavenPublishing {
    publishToMavenCentral(host = SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(
        groupId = project.property("project.group").toString(),
        artifactId = project.property("project.artifact").toString(),
        version = project.property("project.version").toString()
    )

    pom {
        name.set(project.property("project.artifact").toString())
        description.set(project.property("publish.description").toString())
        inceptionYear.set(Calendar.getInstance().get(Calendar.YEAR).toString())
        url.set("https://github.com/NelminDev/${project.property("project.artifact")}")
        licenses {
            license {
                name.set("GPL-3.0")
                url.set("https://opensource.org/licenses/GPL-3.0")
            }
        }
        developers {
            developer {
                id.set("nelmindev")
                name.set("Nelmin")
                email.set("me@nelmin.dev")
            }
        }
        scm {
            url.set("https://github.com/NelminDev/${project.property("project.artifact")}")
            connection.set("scm:git:git://github.com/NelminDev/${project.property("project.artifact")}.git")
            developerConnection.set("scm:git:ssh://git@github.com/NelminDev/${project.property("project.artifact")}.git")
        }
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}