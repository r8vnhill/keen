@file:Suppress("SpellCheckingInspection")

// Obtains the version from the ``gradle.properties`` file.
val projectVersion: String by project

plugins {
    // Configures Kotlin to compile to the JVM
    kotlin("jvm") version "1.9.0"
    // Enables publishing artifacts to Maven repositories
    `maven-publish`
    // Enables signing published artifacts
    signing
    // Enables generating documentation
    id("org.jetbrains.dokka") version "1.7.20"
    // Enables checking Kotlin code style
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    kotlin("plugin.serialization") version "1.8.21"
}

// These two lines set the group and version of the project, which are used in the Maven coordinates
// for the published artifacts.
group = "cl.ravenhill"
version = projectVersion

repositories {
    maven("https://www.jitpack.io") {
        name = "jitpack"
    }
    mavenCentral()
}

dependencies {
    // Kotlin standard library with extensions for JDK 8
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20-RC")
    // Library for writing asynchronous code using coroutines in Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    // Library for working with date and time in Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    // Library for working with reflection in Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    // Tablesaw core library for working with tabular data in Java and Kotlin
    api("tech.tablesaw:tablesaw-core:0.43.1")
    // Tablesaw library for creating JS plots in Kotlin/JVM
    @Suppress("VulnerableLibrariesLocal")
    api("tech.tablesaw:tablesaw-jsplot:0.43.1")
    // Simple logging facade for Java
    implementation("org.slf4j:slf4j-simple:2.0.5")
    // JSON serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.kotest.extensions:kotest-property-datetime:1.1.0")
    // Kotest library for writing data-driven tests in Kotlin
    testImplementation("io.kotest:kotest-framework-datatest:5.5.5")
    // Kotest library for writing assertions in Kotlin
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    // Kotest library for running tests with JUnit 5 in Kotlin
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    // Kotest library for writing property-based tests in Kotlin
    testImplementation("io.kotest:kotest-property:5.5.5")
    // Library for testing code that uses System.exit() in Java/Kotlin
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
    // Dokka plugin for generating documentation in HTML format
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.8.20-dev-213")
}

java {
    // Generates a Javadoc jar file containing the Javadoc for this project's public API
    withJavadocJar()
    // Generates a sources jar file containing the sources for this project
    withSourcesJar()
}

kotlin {
    // Configures the JVM toolchain to use version 8 of the JDK
    jvmToolchain(8)
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

tasks.test {
    // Configures the test task to use JUnit 5
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            val isSnapshot = projectVersion.endsWith("SNAPSHOT")

            // If the version ends with "SNAPSHOT", publish to the snapshot repository. Otherwise,
            // publish to the release repository.
            val destintation = if (isSnapshot) {
                "https://oss.sonatype.org/content/repositories/snapshots/"
            } else {
                "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            }

            url = uri(destintation)

            credentials {
                if (System.getProperty("os.name").startsWith("Windows")) {
                    username = System.getenv("SonatypeUsername")
                    password = System.getenv("SonatypePassword")
                } else {
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        }
    }
}
