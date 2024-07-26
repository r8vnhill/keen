/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int

/**
 * Generates an arbitrary [Selector] instance for property-based testing in evolutionary algorithms.
 *
 * This function creates instances of [Selector] suitable for use in testing scenarios involving evolutionary
 * algorithms. It provides a default implementation of the `select` ([Selector.select]) method that simply takes the
 * first `count` individuals from the population. This basic behavior is typically sufficient for testing purposes where
 * the focus is not on the selection logic itself but on other aspects of the evolutionary process.
 *
 * ## Usage:
 * This arbitrary generator can be used in property-based testing frameworks like Kotest to create [Selector] instances
 * for use in tests of evolutionary algorithms. It ensures that the selection process is consistent and predictable for
 * test scenarios.
 *
 * ### Example:
 * ```kotlin
 * val selectorArb = Arb.selector<MyDataType, MyGene>()
 * val selector = selectorArb.bind() // Instance of a Selector
 * // Use the selector in evolutionary algorithm testing
 * ```
 * In this example, `selectorArb` generates a simple [Selector] instance. When bound, `selector` can be used
 * in tests to simulate the selection process in an evolutionary algorithm.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @return An [Arb] that generates [Selector] instances with a simple selection logic.
 */
fun <T, G> Arb.Companion.selector(): Arb<Selector<T, G>> where G : Gene<T, G> = arbitrary {
    object : Selector<T, G> {
        override fun select(
            population: Population<T, G>,
            count: Int,
            ranker: FitnessRanker<T, G>,
        ) = population.take(count)
    }
}

/**
 * Creates an arbitrary generator for [TournamentSelector]<[T], [G]> instances.
 *
 * This function, part of the [Arb.Companion] object, generates arbitrary instances of `TournamentSelector<T, G>`.
 * The `TournamentSelector` is a selection mechanism used in evolutionary algorithms to choose individuals for
 * reproduction based on their fitness. It operates by randomly selecting a subset of individuals (a tournament)
 * and then choosing the best individual from this subset. The size of each tournament is determined by the provided
 * `tournamentSize` arbitrary, which defaults to a range between 1 and 5.
 *
 * ## Functionality:
 * - Generates `TournamentSelector` instances with the tournament size determined by `tournamentSize`.
 * - The tournament size specifies the number of individuals to be compared in each selection round.
 *
 * ## Usage:
 * Use this arbitrary to generate `TournamentSelector` instances in scenarios involving genetic algorithms where
 * tournament-based selection is required. This method is particularly useful for testing different configurations
 * of tournament sizes in your selection strategies.
 *
 * ### Example:
 * ```kotlin
 * val tournamentSelectorGen = KeenArb.tournamentSelector<Int, SomeGeneClass>()
 * val tournamentSelector = tournamentSelectorGen.bind() // Generates a TournamentSelector instance
 * // Use tournamentSelector in an evolutionary algorithm for selection of individuals
 * ```
 * In this example, `tournamentSelectorGen` is an arbitrary that generates a `TournamentSelector` instance, which can
 * be used in evolutionary algorithms to select individuals based on their fitness in a tournament-style competition.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param tournamentSize An optional [Arb]<[Int]> that determines the size of the tournament. Defaults to a range
 *                       between 1 and 5.
 * @return An [Arb]<[TournamentSelector]<[T], [G]>> for generating `TournamentSelector` instances with varying
 *         tournament sizes.
 */
fun <T, G> arbTournamentSelector(
    tournamentSize: Arb<Int> = Arb.int(1..5)
) where G : Gene<T, G> = arbitrary {
    TournamentSelector<T, G>(tournamentSize.bind())
}

/**
 * Creates an arbitrary generator for [RouletteWheelSelector]<[T], [G]> instances.
 *
 * This function is a part of the [Arb.Companion] object and generates arbitrary instances of
 * `RouletteWheelSelector<T, G>`. The `RouletteWheelSelector` implements a selection mechanism commonly used in genetic
 * algorithms, where the chance of an individual being selected is proportional to its fitness. The selector can operate
 * in two modes, determined by the `sorted` parameter: if `true`, it assumes individuals are pre-sorted by fitness; if
 * `false`, it will handle sorting internally.
 *
 * ## Functionality:
 * - Generates `RouletteWheelSelector` instances with behavior determined by the `sorted` parameter.
 * - When `sorted` is `true`, it assumes the individuals are already sorted by their fitness.
 * - When `sorted` is `false`, it performs sorting of individuals based on fitness internally.
 *
 * ## Usage:
 * Utilize this arbitrary to generate `RouletteWheelSelector` instances in scenarios involving genetic algorithms where
 * a fitness-proportional selection is required. Especially useful for testing the impact of pre-sorted and non-sorted
 * populations on selection efficiency and algorithm performance.
 *
 * ### Example:
 * ```kotlin
 * val rouletteWheelSelectorGen = Arb.rouletteWheelSelector<Boolean>(Arb.boolean())
 * val rouletteWheelSelector = rouletteWheelSelectorGen.bind() // Generates a RouletteWheelSelector instance
 * // Use rouletteWheelSelector in a genetic algorithm for fitness-proportional selection
 * ```
 * In this example, `rouletteWheelSelectorGen` is an arbitrary that generates a `RouletteWheelSelector` instance. This
 * selector can then be used in genetic algorithms to select individuals based on their relative fitness, with or
 * without assuming a pre-sorted population.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param sorted An [Arb]<[Boolean]> that determines the mode of operation for the selector. If `true`, assumes
 *   individuals are sorted by fitness.
 * @return An [Arb]<[RouletteWheelSelector]<[T], [G]>> for generating `RouletteWheelSelector` instances.
 */
fun <T, G> arbRouletteWheelSelector(
    sorted: Arb<Boolean> = Arb.boolean(),
) where G : Gene<T, G> = arbitrary {
    RouletteWheelSelector<T, G>(sorted.bind())
}
