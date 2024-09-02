/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.utils

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind


/**
 * Generates an arbitrary `Individual` for use in evolutionary algorithms.
 *
 * @param arbRepresentation An `Arb<R>` generator for creating random representations.
 * @param arbFitness An optional `Arb<Double>` generator for creating random fitness values. Defaults to
 *   `arbNonNanDouble()`.
 * @return An `Arb<Individual<T, F, R>>` generator that produces random `Individual` instances.
 */
fun <T, F, R> arbIndividual(
    arbRepresentation: Arb<R>,
    arbFitness: Arb<Double> = arbNonNanDouble()
) where F : Feature<T, F>,
        R : Representation<T, F> =
    Arb.bind(arbRepresentation, arbFitness) { representation, fitness ->
        Individual(representation, fitness)
    }
