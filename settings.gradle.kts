pluginManagement {
    includeBuild("convention-plugins")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS

    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/r8vnhill/strait-jakt")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        mavenCentral()
        google()
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "SonatypeSnapshots"
            mavenContent { snapshotsOnly() }
        }

        //region Declare the Node.js & Yarn download repositories
        // Workaround https://youtrack.jetbrains.com/issue/KT-68533/
        ivy("https://nodejs.org/dist/") {
            name = "Node Distributions at $url"
            patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
            metadataSources { artifact() }
            content { includeModule("org.nodejs", "node") }
        }
        ivy("https://github.com/yarnpkg/yarn/releases/download") {
            name = "Yarn Distributions at $url"
            patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
            metadataSources { artifact() }
            content { includeModule("com.yarnpkg", "yarn") }
        }
        //endregion

        mavenLocal()
    }
}

rootProject.name = "keen"
include(":test-utils")
include(":keen-core")
include(":keen-genetics")
