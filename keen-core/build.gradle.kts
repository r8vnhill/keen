import org.jetbrains.dokka.gradle.DokkaTask

/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

val dokkaVersion = extra["dokka.version"] as String
val jaktVersion = extra["jakt.version"] as String
val kotestVersion = extra["kotest.version"] as String
val kotlinxDatetimeVersion = extra["kotlinx.datetime.version"] as String
val letsPlotKotlinVersion = extra["lets-plot.kotlin.version"] as String
val letsPlotSkiaVersion = extra["lets-plot.skia.version"] as String
val letsPlotVersion = extra["lets-plot.version"] as String
val slf4jVersion = extra["slf4j.version"] as String

plugins {
    `maven-publish`
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.compose")
    id("org.jetbrains.dokka")
    kotlin("jvm")
    signing
}

dependencies {
    api("cl.ravenhill:strait-jakt:$jaktVersion")
    api(compose.desktop.common)
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:$dokkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-common:$letsPlotVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-compose:$letsPlotSkiaVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-kernel:$letsPlotKotlinVersion")
    implementation("org.jetbrains.lets-plot:platf-awt:$letsPlotVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}

val dokkaHtml by tasks.getting(DokkaTask::class)

val dokkaJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "cl.ravenhill"
            artifactId = "keen-core"
            version = project.version.toString()
            from(components["kotlin"])
            artifact(dokkaJar)
            artifact(sourcesJar)
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
                scm {
                    url.set("https://github.com/r8vnhill/keen")
                    connection.set("scm:git:git://github.com/r8vnhill/keen.git")
                    developerConnection.set("scm:git:ssh://github.com/r8vnhill/keen.git")
                }
                developers {
                    developer {
                        id.set("r8vnhill")
                        name.set("Ignacio Slater M.")
                        email.set("reachme@ravenhill.cl")
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

    repositories {
        maven {
            name = "OSSRH"
            url = if (project.version.toString().endsWith("SNAPSHOT")) {
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            } else {
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = if (System.getProperty("os.name").startsWith("Windows")) {
                    System.getenv("SonatypeUsername")
                } else {
                    System.getenv("SONATYPE_USERNAME")
                }.apply { check(!isNullOrEmpty()) { "Sonatype username not found." } }
                password = if (System.getProperty("os.name").startsWith("Windows")) {
                    System.getenv("SonatypePassword")
                } else {
                    System.getenv("SONATYPE_PASSWORD")
                }.apply { check(!isNullOrEmpty()) { "Sonatype password not found." } }
            }
        }
    }
}
