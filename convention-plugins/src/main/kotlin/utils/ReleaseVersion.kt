/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package utils

import org.gradle.api.artifacts.VersionCatalog
import kotlin.jvm.optionals.getOrElse

/**
 * A class that represents a release version, derived from a version catalog.
 *
 * @property versionCatalog The version catalog used to retrieve the version information.
 */
class ReleaseVersion(private val versionCatalog: VersionCatalog) {

    /**
     * The version of the 'jakt' library as defined in the version catalog. Throws an error if the version is not found.
     */
    val jaktVersion: String =
        versionCatalog.findVersion("jakt").getOrElse { error("Version for 'jakt' not found") }.requiredVersion

    /**
     * A boolean property that indicates whether the version is a release version. It is considered a release version if
     * it does not contain "-SNAPSHOT" or "-LOCAL".
     */
    val isRelease = "-SNAPSHOT" !in jaktVersion && "-LOCAL" !in jaktVersion
}
