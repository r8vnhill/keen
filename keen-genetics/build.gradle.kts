/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

val jaktVersion = extra["jakt.version"] as String
val kotestVersion = extra["kotest.version"] as String

plugins {
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
}

dependencies {
    implementation(project(":keen-core"))
    implementation("cl.ravenhill:strait-jakt:$jaktVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}
