val kotlinVersion = extra["kotlin.version"] as String
plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(mapOf("path" to ":core")))
    implementation(kotlin("reflect"))
}
