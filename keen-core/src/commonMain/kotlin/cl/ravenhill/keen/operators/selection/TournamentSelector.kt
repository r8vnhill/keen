/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BePositive
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
 * Let's consider a population of individuals with fitness values `[1, 2, 3, 4, 5]` and a tournament size of 3. The
 * tournament selection process randomly selects three individuals from the population, such as `[2, 4, 5]`, and then
 * picks the individual with the highest fitness value, which is 5. The process repeats until it selects the desired
 * number of individuals
 *
 * ## Usage:
 * Use this class when implementing evolutionary algorithms that require a tournament selection process. The tournament
 * size can be configured to control the selective pressure—larger tournament sizes increase the chances of selecting
 * fitter individuals.
 *
 * ### Example:
 * Performing tournament selection with a population:
 * ```kotlin
 * val state: MyEvolutionState = // ...
 * val selector = TournamentSelector<MyType, MyFeature, MyRepresentation>(tournamentSize = 5)
 * val selectedPopulation = selector(state = state, outputSize = 10) { it.copy() }
 * ```
 * In this example, the `TournamentSelector` selects 10 individuals from the population using a tournament size of 5.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property tournamentSize The size of the tournament, determining how many individuals are compared in each
 *   selection round. Must be positive.
 * @constructor Creates a `TournamentSelector` with the specified tournament size.
 * @throws SelectionException if the tournament size is not positive.
 */
data class TournamentSelector<T, F, R>(
    private val tournamentSize: Int = DEFAULT_TOURNAMENT_SIZE
) : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    init {
        constrained {
            "Tournament size ($tournamentSize) must be positive"(::SelectionException) {
                tournamentSize must BePositive
            }
        }.getOrElse { throw it }
    }

    override suspend fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>
    ): Either<SelectionException, Population<T, F, R>> =
        if (population.isEmpty()) {
            SelectionException("Population cannot be empty").left()
        } else {
            // Perform selection
            (0..<count).mapNotNull {
                generateSequence { population[Domain.random.nextInt(population.size)] }
                    .take(tournamentSize)
                    .maxWithOrNull(ranker.comparator)
            }
                .constrainedTo {
                    "The number of selected individuals (${it.size}) must match the requested count ($count)" {
                        it must HaveSize(count)
                    }
                }
                .getOrElse {
                    return SelectionException(
                        "Failed to select the required number of individuals",
                        it
                    ).left()
                }
                .right()
        }

    companion object {
        /**
         * The default size of the tournament, set to 3.
         */
        private const val DEFAULT_TOURNAMENT_SIZE = 3
    }
}
