/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.repr.Feature
import io.kotest.property.Shrinker

/**
 * A shrinker for evolutionary algorithm features.
 *
 * @param T The type of the value held by the feature.
 * @param F The type of the feature, which must extend [Feature].
 */
interface FeatureShrinker<T, F> : Shrinker<F> where F : Feature<T, F>
