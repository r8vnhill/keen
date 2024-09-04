/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Shrinker

/**
 * Shrinker for representations in evolutionary algorithms.
 *
 * @param T The type of the value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface RepresentationShrinker<T, F, R> : Shrinker<R> where F : Feature<T, F>, R : Representation<T, F>
