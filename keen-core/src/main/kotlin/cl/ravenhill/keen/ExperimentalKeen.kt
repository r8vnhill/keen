package cl.ravenhill.keen

/**
 * Marks an API as experimental within the Keen library. This annotation indicates that the API is still in
 * a state of flux and may undergo changes in the future. Users of the API must acknowledge the experimental
 * status and potential instability by opting in to use it.
 *
 * ## Usage:
 * - Apply this annotation to any class, function, property, or type alias that is part of the experimental API.
 * - When using an element marked with `ExperimentalKeen`, the user must explicitly opt in to acknowledge the
 *   potential risks associated with using an experimental API.
 *
 * ## Opt-in Requirement:
 * Users can opt in to using the experimental API either by annotating their usage with
 * `@OptIn(ExperimentalKeen::class)` or by adding a compiler argument in their build script.
 *
 * ## Attributes:
 * - `level`: Indicates the severity of the warning when the API is used without opting in. For `ExperimentalKeen`,
 *   the level is set to `RequiresOptIn.Level.WARNING`, meaning that using the API without opting in will generate a
 *   compiler warning.
 * - `message`: Provides a custom message that clarifies the experimental nature of the API.
 *
 * ## Example:
 * ```
 * @ExperimentalKeen
 * class ExperimentalFeature {
 *     // Implementation of the experimental feature
 * }
 *
 * // Opting in to use the experimental feature
 * @OptIn(ExperimentalKeen::class)
 * fun useExperimentalFeature() {
 *     val feature = ExperimentalFeature()
 *     // ...
 * }
 * ```
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING, message = "This API is experimental and may change in the future.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.TYPEALIAS)
annotation class ExperimentalKeen
