rootProject.name = "convention-plugins"

pluginManagement {
   repositories {
      mavenCentral()
      gradlePluginPortal()
   }
}

dependencyResolutionManagement {
   repositoriesMode = RepositoriesMode.PREFER_SETTINGS
   @Suppress("UnstableApiUsage")
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
