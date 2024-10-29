package cl.ravenhill.keen

/**
 * Annotation to signal that a component's documentation may be outdated and requires review.
 *
 * The `OutdatedDocumentation` annotation is intended to mark classes, functions, properties, or other components
 * where recent changes in the code may have rendered the existing documentation inaccurate or incomplete. It serves
 * as a reminder for developers that both the in-code comments and the corresponding external documentation may need
 * to be updated to reflect the latest modifications.
 *
 * This annotation is primarily used to ensure that documentation consistency is maintained across the codebase and
 * external documentation platforms, helping prevent discrepancies between the code and its documentation.
 *
 * ## Usage:
 * Apply this annotation to any component where recent code changes may have impacted the accuracy of the documentation.
 * Developers reviewing or modifying these components should ensure that the documentation is updated accordingly.
 *
 * ### Example:
 * ```kotlin
 * @OutdatedDocumentation(reason = "Modified function signature, update the parameter descriptions.")
 * fun processData(input: String): Result {
 *     // Function implementation
 * }
 * ```
 *
 * @param reason A brief explanation of why the documentation may be outdated, helping reviewers understand the nature
 *   of the changes that triggered this annotation.
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.ANNOTATION_CLASS
)
@Retention(AnnotationRetention.SOURCE)
@OutdatedDocumentation("Since this annotation is new, it is considered outdated by default.")
annotation class OutdatedDocumentation(val reason: String = "")
