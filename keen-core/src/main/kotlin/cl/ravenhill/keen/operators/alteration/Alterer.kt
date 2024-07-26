/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.operators.Operator


/**
 * Represents an alterer in an evolutionary algorithm.
 *
 * The `Alterer` interface extends the `Operator` interface and defines the basic structure and operations for
 * altering individuals in the population. This includes a method to combine multiple alterers using the plus operator.
 *
 * ## Usage:
 * Implement this interface in classes that perform specific alteration operations on the state of the evolutionary
 * algorithm, such as mutation or crossover. Provide the logic to transform the population accordingly.
 *
 * ### Example:
 * ```kotlin
 * class MutationAlterer<T, F> : Alterer<T, F> where F : Feature<T, F> {
 *
 *     override fun <S, I> invoke(
 *         state: S,
 *         outputSize: Int,
 *         buildState: (List<I>) -> S
 *     ): S where S : State<T, F, I>, I : FitnessEvaluable {
 *         // Implementation of the mutation logic
 *     }
 * }
 *
 * class CrossoverAlterer<T, F> : Alterer<T, F> where F : Feature<T, F> {
 *
 *     override fun <S, I> invoke(
 *         state: S,
 *         outputSize: Int,
 *         buildState: (List<I>) -> S
 *     ): S where S : State<T, F, I>, I : FitnessEvaluable {
 *         // Implementation of the crossover logic
 *     }
 * }
 *
 * val combinedAlterers = MutationAlterer<MyGeneType, MyFeatureType>() + CrossoverAlterer()
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 */
interface Alterer<T, F> : Operator<T, F> where F : Feature<T, F> {

    /**
     * Combines this alterer with another alterer using the plus operator.
     *
     * This method allows combining multiple alterers into a list for sequential application.
     *
     * @param alterer The alterer to combine with this alterer.
     * @return A list containing this alterer and the specified alterer.
     */
    operator fun plus(alterer: Alterer<T, F>) = listOf(this, alterer)
}
