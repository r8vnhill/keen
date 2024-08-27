/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import kotlin.random.Random

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
     * The function wraps the selection operation in a [runCatching] block, meaning that any exceptions encountered
     * during the selection process are caught and returned as part of a [Result].
     *
     * ## Potential Exceptions:
     * The following exceptions may be generated during the selection process and will be wrapped in a [Result]:
     * - [SelectionException]: Thrown if the tournament fails to find a valid individual, which might occur if the
     *   population is empty or other unexpected conditions arise.
     *
     * @param population The population from which individuals are selected.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to compare individuals during the selection process.
     * @param random A random number generator used for selecting individuals for each tournament.
     * @return A [Result] containing the selected population, or an exception wrapped in the [Result] if the selection
     *   fails.
     */
    override fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>,
        random: Random
    ): Result<Population<T, F, R>> = runCatching {
        (0..<count).map {
            generateSequence { population[random.nextInt(population.size)] }
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
