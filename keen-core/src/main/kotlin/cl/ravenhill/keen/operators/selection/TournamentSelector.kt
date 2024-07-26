/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.operators.selection.TournamentSelector.Companion.DEFAULT_SIZE
import cl.ravenhill.keen.ranking.Ranker
import java.util.Objects.hash


/**
 * Represents a tournament selector in an evolutionary algorithm.
 *
 * The `TournamentSelector` class implements the `Selector` interface and provides a selection mechanism based on
 * tournaments. You select individuals by randomly choosing a specified number of candidates from the population and
 * selecting the best individual among them. Repeat this process until you select the desired number of individuals.
 *
 * ## Usage:
 * Use this class to select individuals from a population in an evolutionary algorithm based on their fitness.
 * Control the selection process with the tournament size, which determines the number of candidates in each tournament.
 *
 * ### Example:
 * ```kotlin
 * val tournamentSelector = TournamentSelector<MyGeneType, MyFeatureType>(
 *     tournamentSize = 5
 * )
 * val selectedState = tournamentSelector(currentState, 10)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property tournamentSize The number of candidates in each tournament. Default value is [DEFAULT_SIZE].
 * @constructor Creates an instance of `TournamentSelector` with the specified tournament size and state builder.
 */
class TournamentSelector<T, F>(
    val tournamentSize: Int = DEFAULT_SIZE,
) : Selector<T, F> where F : Feature<T, F> {

    init {
        constraints {
            "The tournament size ($tournamentSize) must be positive" { tournamentSize must BePositive }
        }
    }

    /**
     * Selects individuals from the population based on their fitness using tournament selection.
     *
     * This method performs the selection process by randomly choosing a specified number of candidates from the
     * population and selecting the best individual among them. Repeat this process until you select the desired number
     * of individuals.
     *
     * @param population The population of individuals to select from.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate individuals in the population.
     * @return The list of selected individuals.
     */
    override fun select(population: List<FitnessEvaluable>, count: Int, ranker: Ranker<T, F>) =
        (0..<count).map {
            generateSequence { population[Domain.random.nextInt(population.size)] }
                .take(tournamentSize)
                .maxWithOrNull(ranker.comparator)
                ?: throw SelectionException("Tournament selection failed to find a max individual")
        }

    /**
     * Returns a string representation of the `TournamentSelector`.
     *
     * @return The string "TournamentSelector(tournamentSize=<size>)".
     */
    override fun toString() = "TournamentSelector(tournamentSize=$tournamentSize)"

    /**
     * Checks if this `TournamentSelector` is equal to another object.
     *
     * @param other The other object to compare.
     * @return `true` if the other object is a `TournamentSelector` with the same tournament size, `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is TournamentSelector<*, *> -> false
        else -> tournamentSize == other.tournamentSize
    }

    /**
     * Returns the hash code of this `TournamentSelector`.
     *
     * @return The hash code of this `TournamentSelector`.
     */
    override fun hashCode() = hash(TournamentSelector::class, tournamentSize)

    companion object {
        /**
         * The default tournament size. Default value is 3.
         */
        const val DEFAULT_SIZE = 3
    }
}
