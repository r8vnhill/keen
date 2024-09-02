/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.utils

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list


/**
 * Generates an arbitrary population of individuals for evolutionary algorithms.
 *
 * @param T The type of value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param arbIndividual An [Arb] generator for `Individual` instances, defining the characteristics of each individual
 *   in the population.
 * @param size An optional [IntRange] specifying the range for the population size. Defaults to 0..100.
 * @return An [Arb] generator that produces a list of `Individual<T, F, R>` instances, representing a population.
 */
fun <T, F, R> arbPopulation(
    arbIndividual: Arb<Individual<T, F, R>>,
    size: IntRange = 0..100
) where F : Feature<T, F>, R : Representation<T, F> = Arb.list(arbIndividual, size)
