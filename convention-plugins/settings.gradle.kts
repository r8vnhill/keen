rootProject.name = "convention-plugins"

pluginManagement {
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
   repositoriesMode = RepositoriesMode.PREFER_SETTINGS
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
