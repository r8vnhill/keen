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
    implementation("cl.ravenhill:strait-jakt:$jaktVersion")
    implementation("io.kotest:kotest-property:$kotestVersion")
    implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}
