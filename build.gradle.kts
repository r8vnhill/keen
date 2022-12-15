import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitVersion: String by project

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.diffplug.spotless") version "6.10.0"
    `maven-publish`
    `java-library`
    id("org.jetbrains.dokka") version "1.7.20"
}

group = "cl.ravenhill"
version = "1.2.1"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin / Kotlinx
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // Lang3
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // Tablesaw
    api("tech.tablesaw:tablesaw-core:0.43.1")
    @Suppress("VulnerableLibrariesLocal")
    api("tech.tablesaw:tablesaw-jsplot:0.43.1")
    // SLF4J
    implementation("org.slf4j:slf4j-simple:2.0.5")
    // Kotest
    implementation("io.kotest:kotest-framework-datatest:5.5.4")
    implementation("io.kotest:kotest-assertions-core:5.5.4")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest:kotest-property:5.5.4")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
    // Dokka
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
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
            version = "1.2.1"
            from(components["java"])
        }
    }
}