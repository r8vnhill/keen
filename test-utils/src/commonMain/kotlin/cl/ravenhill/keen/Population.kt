/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import arrow.core.getOrElse
import cl.ravenhill.InvalidGeneratorException
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arrow.core.nonEmptyList


/**
 * Generates an arbitrary `Population` of individuals for use in property-based testing.
 *
 * @param individualArb An `Arb` instance that generates individual elements of the population.
 * @param size A range specifying the allowed size of the population. Defaults to `0..100`.
 * @return An `Arb` instance that generates a `Population` of individuals within the specified size range.
 * @throws InvalidGeneratorException if the `size` range is invalid.
 */
fun <T, F, R> arbPopulation(individualArb: Arb<Individual<T, F, R>>, size: IntRange = 0..100)
        where F : Feature<T, F>,
              R : Representation<T, F> =
    Arb.list(individualArb, size)
        .map { it.toPopulation() }

/**
 * Generates an arbitrary `NonEmptyPopulation` of individuals for use in property-based testing.
 *
 * @param individualArb An `Arb` instance that generates individual elements of the population.
 * @param size A range specifying the allowed size of the population. Defaults to `1..100`.
 * @return An `Arb` instance that generates a `NonEmptyPopulation` of individuals within the specified size range.
 * @throws InvalidGeneratorException if the `size` range is invalid or non-positive.
 */
fun <T, F, R> arbNonEmptyPopulation(individualArb: Arb<Individual<T, F, R>>, size: IntRange = 1..100)
        where F : Feature<T, F>,
              R : Representation<T, F> = Arb.nonEmptyList(
    individualArb,
    size.constrainedTo { "The size must be positive" { size.first must BePositive } }
        .getOrElse { throw InvalidGeneratorException(it) }
).map { nonEmptyPopulationOf(it) }
