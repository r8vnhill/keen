val kotlinVersion = extra["kotlin.version"] as String
plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(mapOf("path" to ":keen-core")))
    implementation(kotlin("reflect"))
}
