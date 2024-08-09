/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package utils

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension


/**
 * Extension property to get the SigningExtension of the project.
 */
internal val Project.signing
    get() = extensions.getByType<SigningExtension>()

/**
 * Extension function to configure the SigningExtension of the project.
 */
internal fun Project.signing(configure: SigningExtension.() -> Unit = {}) {
    signing.configure()
}

/**
 * Extension property to get the PublishingExtension of the project.
 * ```
 */
internal val Project.publishing
    get() = extensions.getByType<PublishingExtension>()

/**
 * Extension function to configure the PublishingExtension of the project.
 */
internal fun Project.publishing(configure: PublishingExtension.() -> Unit = {}) {
    publishing.configure()
}
