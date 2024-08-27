/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * A selection mechanism that uses tournament selection in an evolutionary algorithm.
 *
 * The `TournamentSelector` class implements a selection strategy where a subset (tournament) of individuals is
 * randomly chosen from the population, and the best individual within this subset is selected based on their fitness.
 * This process is repeated until the desired number of individuals is selected.
 *
 * ## Usage:
 * Use this class when implementing evolutionary algorithms that require a tournament selection process. The tournament
 * size can be configured to control the selective pressure—larger tournament sizes increase the chances of selecting
 * fitter individuals.
 *
 * ### Example:
 * Performing tournament selection with a population:
 * ```kotlin
 * val selector = TournamentSelector<MyType, MyFeature, MyRepresentation>(tournamentSize = 5)
 * val selectedPopulation = selector.select(population, count = 10, ranker = myRanker)
 * ```
 * In this example, the `TournamentSelector` selects 10 individuals from the population using a tournament size of 5.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property tournamentSize The size of the tournament, determining how many individuals are compared in each
 *   selection round. Must be positive.
 * @constructor Creates a `TournamentSelector` with the specified tournament size.
 * @throws CompositeException containing the constraints that were violated.
 * @throws SelectionException if the tournament size is not positive; wrapped in a [CompositeException].
 */
data class TournamentSelector<T, F, R>(
    private val tournamentSize: Int = DEFAULT_TOURNAMENT_SIZE
) : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    init {
        constraints {
            "Tournament size ($tournamentSize) must be positive"(::SelectionException) {
                tournamentSize must BePositive
            }
        }
    }

    /**
     * Selects a subset of the population using the tournament selection method.
     *
     * This method randomly picks a subset of individuals (determined by the tournament size) from the population and
     * selects the best individual within this subset according to the provided ranker. This process is repeated until
     * the specified number of individuals (`count`) is selected.
     *
     * @param population The population from which to select individuals.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to compare individuals during the selection process.
     * @return A population consisting of the selected individuals.
     * @throws SelectionException if the selection process fails to find a valid individual.
     */
    override fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>
    ): Result<Population<T, F, R>> = runCatching {
        (0..<count).map {
            generateSequence { population[Domain.random.nextInt(population.size)] }
                .take(tournamentSize)
                .maxWithOrNull(ranker.comparator)
                ?: throw SelectionException("Tournament selection failed to find a max individual")
        }
    }

    companion object {
        /**
         * The default size of the tournament, set to 3.
         */
        private const val DEFAULT_TOURNAMENT_SIZE = 3
    }
}
