/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

val kotestVersion = extra["kotest.version"] as String
val jaktVersion = extra["jakt.version"] as String
val kotlinxDatetimeVersion = extra["kotlinx.datetime.version"] as String
val javafxVersion = extra["javafx.version"] as String
val letsPlotVersion = extra["lets-plot.version"] as String
val letsPlotKotlinVersion = extra["lets-plot.kotlin.version"] as String
val slf4jVersion = extra["slf4j.version"] as String
val letsPlotSkiaVersion = extra["lets-plot.skia.version"] as String

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.github.r8vnhill:strait-jakt:$jaktVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-kernel:$letsPlotKotlinVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-common:$letsPlotVersion")
    implementation("org.jetbrains.lets-plot:platf-awt:$letsPlotVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-compose:$letsPlotSkiaVersion")
    implementation("org.slf4j:slf4j-simple:2.0.9")  // Enable logging to console

}

tasks.test {
    useJUnitPlatform()
}

java {
    // Generates a Javadoc jar file containing the Javadoc for this project's public API
    withJavadocJar()
    // Generates a sources jar file containing the sources for this project
    withSourcesJar()
}

