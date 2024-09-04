/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.element


/**
 * Generates an arbitrary fitness maximization ranker for testing purposes.
 *
 * @return An `Arb` that randomly selects between a synchronous or asynchronous fitness maximization ranker.
 * @param T The type of value held by each feature.
 * @param F The type of the feature, which must implement the [Feature] interface.
 * @param R The type of the representation, which must implement the [Representation] interface.
 */
fun <T, F, R> arbFitnessMaxRanker()
        where F : Feature<T, F>,
              R : Representation<T, F> = Arb.element(FitnessMaxRanker.sync<_, _, R>(), FitnessMaxRanker.async())

/**
 * Generates an arbitrary fitness maximization ranker for testing purposes.
 *
 * @return An `Arb` that randomly selects between a synchronous or asynchronous fitness maximization ranker.
 */
fun <T, F, R> arbFitnessMinRanker()
        where F : Feature<T, F>,
              R : Representation<T, F> = Arb.element(FitnessMinRanker.sync<_, _, R>(), FitnessMinRanker.async())

/**
 * Generates an arbitrary ranker for testing purposes.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @return An `Arb` that randomly selects between a fitness maximization or minimization ranker.
 */
fun <T, F, R> arbRanker()
        where F : Feature<T, F>,
              R : Representation<T, F> = Arb.choice(arbFitnessMaxRanker<_, _, R>(), arbFitnessMinRanker())
