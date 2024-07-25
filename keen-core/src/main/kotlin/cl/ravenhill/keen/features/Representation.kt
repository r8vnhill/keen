package cl.ravenhill.keen.features

import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Verifiable

/**
 * Represents a collection of features in an evolutionary algorithm.
 *
 * The `Representation` interface extends the `Verifiable` and `FlatMappable` interfaces, providing additional type
 * constraints specific to features used in evolutionary algorithms. It encapsulates a collection of features and
 * includes a property to get the size of the collection.
 *
 * ## Usage:
 * This interface is used to represent a collection of features that can be manipulated and evaluated within an
 * evolutionary algorithm. It allows for the application of standard operations such as verification, flattening,
 * and transformation, while ensuring that the elements conform to the requirements of the [Feature] interface.
 *
 * ### Example:
 * ```
 * class MyRepresentation<T, F : Feature<T, F>>(override val size: Int) : Representation<T, F> {
 *     // Implementation of Verifiable and FlatMappable methods
 * }
 * ```
 * In this example, `MyRepresentation` implements the `Representation` interface, allowing it to be used in
 * evolutionary algorithms where a collection of features needs to be managed collectively.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property size The size of the collection of features.
 */
interface Representation<T, F> : Verifiable, FlatMappable<T> where F : Feature<T, F> {
    val size: Int
}
