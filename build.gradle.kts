import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitVersion: String by project

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.diffplug.spotless") version "6.10.0"
}

group = "cl.ravenhill"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.kotest:kotest-framework-datatest:5.5.3")
    implementation("io.kotest:kotest-assertions-core:5.5.3")
    implementation("org.jetbrains.kotlinx:dataframe:0.8.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.3")
    testImplementation("io.kotest:kotest-property:5.5.3")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}