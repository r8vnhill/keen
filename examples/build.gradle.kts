
val letsPlotKotlinVersion = extra["lets-plot.kotlin.version"] as String
val letsPlotSkiaVersion = extra["lets-plot.skia.version"] as String
val letsPlotVersion = extra["lets-plot.version"] as String
val slf4jVersion = extra["slf4j.version"] as String

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(project(mapOf("path" to ":keen-core")))
    implementation("org.jetbrains.lets-plot:lets-plot-common:$letsPlotVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-compose:$letsPlotSkiaVersion")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-kernel:$letsPlotKotlinVersion")
    implementation("org.jetbrains.lets-plot:platf-awt:$letsPlotVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation(compose.desktop.currentOs)
}
