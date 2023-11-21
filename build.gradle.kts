import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectVersion: String by project
val kotlinVersion: String by project
val coroutinesVersion: String by project
val apacheLangVersion: String by project
val tablesawVersion: String by project
val kotlinxDatetimeVersion: String by project
val kotlinxSerializationVersion: String by project
val slf4jVersion: String by project
val kotestVersion: String by project
val systemLambdaVersion: String by project
val kotestDatetimeVersion: String by project
val dokkaVersion: String by project
val detektVersion: String by project
val jaktVersion: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    `maven-publish`
    signing
    id("com.diffplug.spotless") version "6.10.0"
    id("org.jetbrains.dokka") version "1.9.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    // Lang3
    implementation("org.apache.commons:commons-lang3:$apacheLangVersion")
    // Tablesaw
    api("tech.tablesaw:tablesaw-core:$tablesawVersion")
    @Suppress("VulnerableLibrariesLocal")
    api("tech.tablesaw:tablesaw-jsplot:$tablesawVersion")
    // SLF4J
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    // Kotest
    testImplementation("com.github.stefanbirkner:system-lambda:$systemLambdaVersion")
    testImplementation("io.kotest.extensions:kotest-property-datetime:$kotestDatetimeVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    // Dokka
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:$dokkaVersion")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    implementation("com.github.r8vnhill:strait-jakt:$jaktVersion")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
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


detekt {
    // Configures the detekt task to use the default detekt configuration
    config.from(files("conf/detekt.yml"))
}

val ossrhRepositoryUrl = if (version.toString().endsWith("SNAPSHOT")) {
    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
} else {
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
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
    repositories {
        maven(ossrhRepositoryUrl) {
            name = "ossrh"
            credentials(PasswordCredentials::class)
        }
    }
}
