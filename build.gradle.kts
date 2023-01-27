import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectVersion: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
    `java-library`
    signing
    id("com.diffplug.spotless") version "6.10.0"
    id("org.jetbrains.dokka") version "1.7.20"
}

group = "cl.ravenhill"
version = projectVersion

repositories {
    maven("https://www.jitpack.io") {
        name = "jitpack"
    }
    mavenCentral()
}

dependencies {
    // Kotlin / Kotlinx
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    // Lang3
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // Tablesaw
    api("tech.tablesaw:tablesaw-core:0.43.1")
    @Suppress("VulnerableLibrariesLocal")
    api("tech.tablesaw:tablesaw-jsplot:0.43.1")
    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.5")  // SLF4J (Tablesaw)
    // Kotest
    testImplementation("io.kotest:kotest-framework-datatest:5.5.4")
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest:kotest-property:5.5.4")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
    // System Lambda
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
    // Dokka
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
}

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(8)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("Keen")
                description.set("A genetic algorithm framework for Kotlin")
                url.set("https://github.com/r8vnhill/keen")
                licenses {
                    license {
                        name.set("Attribution 4.0 International (CC BY 4.0)")
                        url.set("https://creativecommons.org/licenses/by/4.0/legalcode")
                    }
                }
            }
            groupId = "cl.ravenhill"
            artifactId = "keen"
            version = projectVersion
            from(components["java"])
        }
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
