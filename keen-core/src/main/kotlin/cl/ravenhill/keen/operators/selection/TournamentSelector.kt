/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.utils.hash


/**
 * A selector implementation for tournament selection in evolutionary algorithms.
 *
 * `TournamentSelector` is a class that implements the tournament selection method. In this method, a set of individuals
 * is randomly chosen from the population, and the best individual from this subset, determined by the provided ranker,
 * is selected. This process is repeated until the desired number of individuals is selected.
 *
 * ## Parameters:
 * - **tournamentSize**: The size of the tournament, i.e., the number of individuals randomly picked from the population
 *   for each selection round. A larger tournament size generally leads to stronger selection pressure.
 *
 * ## Constraints:
 * - The tournament size must be a positive integer.
 *
 * ## Usage:
 * Tournament selection is often used in genetic algorithms where selection pressure needs to be controlled. It's
 * particularly useful when you want to maintain diversity in the population while still favoring fitter individuals.
 *
 * ### Example:
 * Implementing tournament selection with a specific tournament size:
 * ```kotlin
 * val state = EvolutionState<MyData, MyGene>(/*...*/)
 * val tournamentSelector = TournamentSelector<MyData, MyGene>(tournamentSize = 5)
 *
 * // Select individuals using tournament selection
 * val selectedIndividuals = tournamentSelector(state, 10)
 * ```
 * In this example, `TournamentSelector` is instantiated with a tournament size of 5. The selector is then used to
 * select 10 individuals from the population in the given state. The returned state contains a new population with the
 * selected individuals.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 */
class TournamentSelector<T, G>(val tournamentSize: Int = DEFAULT_SIZE) : Selector<T, G> where G : Gene<T, G> {

    init {
        constraints {
            "The tournament size ($tournamentSize) must be positive" { tournamentSize must BePositive }
        }
    }

    /**
     * Selects a subset of individuals from the population using tournament selection.
     *
     * In tournament selection, a number of 'tournaments' are held to select individuals. In each tournament, a subset
     * of individuals is randomly chosen from the population, and the best individual from this subset (determined by
     * the provided ranker) is selected. This process is repeated until the desired number of individuals is selected.
     *
     * ## Process:
     * 1. **Tournament Rounds**: The method runs a total of `count` tournaments.
     * 2. **Random Selection**: In each tournament, `tournamentSize` individuals are randomly selected from the
     *   population.
     * 3. **Determining the Winner**: The best individual from these selected is determined using the `ranker`.
     *
     * ## Example:
     * ```kotlin
     * val population = /* A population of individuals */
     * val tournamentSize = 5
     * val tournamentSelector = TournamentSelector<MyData, MyGene>(tournamentSize)
     * val selectedIndividuals = tournamentSelector.select(population, 10, myRanker)
     * ```
     * In this example, `select` is used within a `TournamentSelector` to choose 10 individuals from the population. For
     * each individual selected, a mini-tournament of size 5 is conducted to determine the best individual based on the
     * specified ranker.
     *
     * @param population The population of individuals from which to select.
     * @param count The number of individuals to select.
     * @param ranker The [IndividualRanker] used to determine the best individual in each tournament.
     *
     * @return A list of individuals selected through tournament selection.
     */
    override fun select(population: Population<T, G>, count: Int, ranker: IndividualRanker<T, G>) =
        (0..<count).map {
            generateSequence { population[Domain.random.nextInt(population.size)] }
                .take(tournamentSize)
                .maxWithOrNull(ranker.comparator)
                ?: throw SelectionException {
                    // Unreachable
                    "Tournament selection failed to find a max individual"
                }
        }

    override fun toString() = "TournamentSelector(tournamentSize=$tournamentSize)"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is TournamentSelector<*, *> -> false
        else -> tournamentSize == other.tournamentSize
    }

    override fun hashCode() = hash(TournamentSelector::class, tournamentSize)

    companion object {
        /** The default size of the tournaments. Set to 3. */
        const val DEFAULT_SIZE = 3
    }
}
