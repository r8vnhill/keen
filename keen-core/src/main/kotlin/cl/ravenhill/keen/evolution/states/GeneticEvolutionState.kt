/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.states

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker
import java.util.*


/**
 * Represents the state of an evolution process in a gene-based evolutionary algorithm.
 *
 * The `GeneticEvolutionState` class encapsulates the current generation, ranker, and population of individuals in an
 * evolutionary process. It implements the `State` interface and provides various utility methods for manipulating and
 * querying the state.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property generation The current generation number.
 * @property ranker The ranker used to evaluate individuals in the population.
 * @property population The population of individuals at the current generation.
 * @property size The size of the population.
 * @constructor Creates an instance of `GeneticEvolutionState` with the specified generation, ranker, and population.
 * @throws CompositeException if any of the constraints are violated.
 * @throws IntConstraintException if the generation is negative.
 */
data class GeneticEvolutionState<T, G>(
    val generation: Int,
    val ranker: IndividualRanker<T, G>,
    override val population: Population<T, G>,
) : State<T, G> where G : Gene<T, G> {

    init {
        constraints { "Generation [$generation] must not be negative" { generation mustNot BeNegative } }
    }

    /**
     * Secondary constructor for creating a `GeneticEvolutionState` with a variable number of individuals.
     *
     * This constructor simplifies the creation of a `GeneticEvolutionState` by allowing the direct input of
     * individuals, rather than requiring a pre-constructed list.
     *
     * @param generation The current generation number.
     * @param ranker The ranker used to evaluate individuals in the population.
     * @param individuals The individuals in the population.
     */
    constructor(
        generation: Int,
        ranker: IndividualRanker<T, G>,
        vararg individuals: Individual<T, G>,
    ) : this(generation, ranker, individuals.toList())

    override val size = population.size

    /**
     * Deprecated operator function for advancing to the next generation.
     *
     * @return A new `GeneticEvolutionState` instance with the generation number incremented by one.
     */
    @Deprecated("Use copy() instead", ReplaceWith("copy(generation = generation + 1)"))
    operator fun next() = GeneticEvolutionState(generation + 1, ranker, population)

    /**
     * Checks if the population is empty.
     *
     * @return `true` if the population is empty, `false` otherwise.
     */
    override fun isEmpty() = population.isEmpty()

    /**
     * Applies a transformation function to each individual in the population and returns a new `GeneticEvolutionState`.
     *
     * @param function The transformation function to apply to each individual.
     * @return A new instance of `GeneticEvolutionState` with the transformed population.
     */
    fun map(function: (Individual<T, G>) -> Individual<T, G>) = copy(population = population.map(function))

    companion object {
        /**
         * Creates an empty `GeneticEvolutionState` with the specified ranker.
         *
         * @param T The type of the value held by the genes.
         * @param G The type of the gene, which must extend [Gene].
         * @param ranker The ranker used to evaluate individuals in the population.
         * @return An empty `GeneticEvolutionState`.
         */
        fun <T, G> empty(ranker: IndividualRanker<T, G>) where G : Gene<T, G> = GeneticEvolutionState(0, ranker)
    }
}
