object Ci {

   /**
    * The base version used for the release version.
    *
    * `-SNAPSHOT` or `-LOCAL` will be appended.
    */
   private const val snapshotBase = "2.0.0"

   /** Is the build currently running on CI. */
   private val isCI = System.getenv("CI").toBoolean()

   private val snapshotVersion =
      snapshotBase + if (isCI) "-SNAPSHOT" else "-LOCAL"

   /** The final release version. If specified, will override [snapshotVersion]. */
   private val releaseVersion = System.getenv("RELEASE_VERSION")?.ifBlank { null }

   val isRelease = releaseVersion != null

   /** The published version of Keen dependencies. */
   val publishVersion = releaseVersion ?: snapshotVersion
}
