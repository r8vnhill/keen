/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.repr.Representation

/**
 * Configuration class for the selection process in an evolutionary algorithm.
 *
 * The `SelectionConfig` data class encapsulates the settings and components used for parent and survivor selection
 * during the evolutionary process. This includes the survival rate, parent selector, and survivor selector.
 *
 * ## Usage:
 * This class is used to configure the selection process in the evolutionary algorithm by specifying the necessary
 * selectors and survival rate.
 *
 * ### Example 1: Basic Configuration
 * ```kotlin
 * val config = SelectionConfig<MyValue, MyFeature, MyRepresentation>(
 *     survivalRate = 0.5,
 *     parentSelector = TournamentSelector(),
 *     survivorSelector = RouletteWheelSelector()
 * )
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property survivalRate The rate at which individuals survive to the next generation.
 * @property parentSelector The selector used to choose parents for reproduction.
 * @property survivorSelector The selector used to choose survivors for the next generation.
 * @constructor Creates an instance of `SelectionConfig` with the specified settings and selectors.
 */
data class SelectionConfig<T, F, R>(
    val survivalRate: Double,
    val parentSelector: Selector<T, F, R>,
    val survivorSelector: Selector<T, F, R>
) where F : Feature<T, F>, R : Representation<T, F>
