package cl.ravenhill.keen.annotations

/**
 * Annotation to mark functions or classes as broken.
 *
 * This annotation can be used to indicate that a function or class is known to be broken and should not be used. The
 * optional `message` parameter allows you to provide additional context or information about the issue.
 *
 * ## Usage:
 * This annotation can be applied to both functions and classes.
 *
 * ### Example 1: Annotating a broken function
 * ```
 * @Broken("This function is broken due to a known bug.")
 * fun someFunction() {
 *     // Implementation
 * }
 * ```
 *
 * ### Example 2: Annotating a broken class
 * ```
 * @Broken("This class is broken and needs to be refactored.")
 * class SomeClass {
 *     // Implementation
 * }
 * ```
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING, message = "This feature is broken and should not be used.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.TYPEALIAS)
annotation class Broken(val message: String)
