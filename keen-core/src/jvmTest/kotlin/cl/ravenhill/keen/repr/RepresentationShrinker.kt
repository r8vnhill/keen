package cl.ravenhill.keen.repr

import io.kotest.property.Shrinker

/**
 * A [Shrinker] interface for shrinking [Representation] instances. Implementations of this interface should provide
 * logic to shrink a given [Representation] by reducing its complexity, typically by modifying the underlying
 * feature(s).
 *
 * @param T The type of value stored by the feature.
 * @param F The type of feature used in the representation, which must implement [Feature].
 * @param R The type of representation used, which must implement [Representation].
 */
interface RepresentationShrinker<T, F, R> : Shrinker<R> where F : Feature<T, F>,
                                                              R : Representation<T, F>
