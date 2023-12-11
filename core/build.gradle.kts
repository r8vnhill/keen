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
val dokkaVersion = extra["dokka.version"] as String

plugins {
    `maven-publish`
    id("org.jetbrains.compose")
    id("org.jetbrains.dokka")
    kotlin("jvm")
    signing
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
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:$dokkaVersion")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "cl.ravenhill"
            artifactId = "keen"
            version = project.version.toString()
            from(components["kotlin"])  // or maybe components["java"]
            pom {
                name.set("Keen")
                description.set("A Kotlin library for Evolutionary Computation")
                url.set("https://github.com/r8vnhill/keen")
                licenses {
                    license {
                        name.set("BSD 2-Clause License")
                        url.set("https://opensource.org/licenses/BSD-2-Clause")
                    }
                }
            }
        }
    }

    if (!project.version.toString().endsWith("SNAPSHOT")) {
        signing {
            useGpgCmd()
            sign(publications)
        }
    }
}
