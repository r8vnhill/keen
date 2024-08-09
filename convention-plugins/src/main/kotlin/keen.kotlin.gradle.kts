plugins {
    id("keen.base")
    kotlin("multiplatform")
}


tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    sourceSets.configureEach {
        languageSettings {
            optIn("cl.ravenhill.keen.ExperimentalKeen")
        }
    }
}
