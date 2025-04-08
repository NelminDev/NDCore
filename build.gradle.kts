import com.vanniktech.maven.publish.SonatypeHost
import java.util.Calendar

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
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("io.ktor:ktor-client-cio-jvm:3.1.2")
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("io.ktor:ktor-client-core:3.1.2")
    implementation("io.ktor:ktor-client-cio:3.1.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    api("dev.nelmin:lumina:1.0.2")
}

tasks {
    runServer {
        minecraftVersion("1.21")
    }

    shadowJar {
        archiveClassifier.set("shaded")
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