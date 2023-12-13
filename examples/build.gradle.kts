
val letsPlotKotlinVersion = extra["lets-plot.kotlin.version"] as String
val letsPlotSkiaVersion = extra["lets-plot.skia.version"] as String
val letsPlotVersion = extra["lets-plot.version"] as String

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(mapOf("path" to ":core")))
}
