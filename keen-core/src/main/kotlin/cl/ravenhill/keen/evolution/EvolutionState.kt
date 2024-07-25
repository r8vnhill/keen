/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.utils.hash
import java.util.*


/**
 * Represents the state of an evolution process in an evolutionary algorithm.
 *
 * The `EvolutionState` class encapsulates the current generation, ranker, and population of individuals in an
 * evolutionary process. It provides various utility methods for manipulating and querying the state. This class
 * ensures that constraints on the generation number are met, specifically that the generation number is not negative.
 *
 * ## Usage:
 * This class is used in evolutionary algorithms to represent and manage the state of the evolution process. It
 * can be instantiated with a specific generation, ranker, and population, or with individual members of the population.
 *
 * ### Example:
 * ```
 * val ranker = MyRanker()
 * val population = listOf(Individual(...), Individual(...))
 * val state = EvolutionState(0, ranker, population)
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @property generation The current generation number.
 * @property ranker The ranker used to evaluate individuals in the population.
 * @property population The population of individuals at the current generation.
 * @constructor Creates an instance of `EvolutionState` with the specified generation, ranker, and population.
 * @throws CompositeException if any of the constraints are violated.
 * @throws IntConstraintException if the generation is negative.
 */
open class EvolutionState<T, F>(
    val generation: Int,
    val ranker: IndividualRanker<T, F>,
    val population: Population<T, F>,
) where F : Feature<T, F> {

    init {
        constraints { "Generation [$generation] must not be negative" { generation mustNot BeNegative } }
    }

    /**
     * Secondary constructor for creating an `EvolutionState` instance with a vararg of individuals.
     *
     * This constructor converts the vararg of individuals into a population list.
     *
     * @param generation The current generation number.
     * @param ranker The ranker used to evaluate individuals in the population.
     * @param individuals Vararg of individuals forming the population.
     */
    constructor(
        generation: Int,
        ranker: IndividualRanker<T, F>,
        vararg individuals: Individual<T, F>,
    ) : this(generation, ranker, individuals.toList())

    /**
     * The size of the population.
     */
    val size = population.size

    /**
     * Advances the evolution state to the next generation.
     *
     * @return A new instance of `EvolutionState` with the generation incremented by 1.
     * @deprecated Use [copy] instead.
     */
    @Deprecated("Use copy() instead", ReplaceWith("copy(generation = generation + 1)"))
    operator fun next() = EvolutionState(generation + 1, ranker, population)

    /**
     * Checks if the population is empty.
     *
     * @return `true` if the population is empty, `false` otherwise.
     */
    fun isEmpty() = population.isEmpty()

    /**
     * Creates a copy of the current `EvolutionState` with optionally modified parameters.
     *
     * @param generation The generation number for the new state.
     * @param ranker The ranker for the new state.
     * @param population The population for the new state.
     * @return A new instance of `EvolutionState` with the specified parameters.
     */
    fun copy(
        generation: Int = this.generation,
        ranker: IndividualRanker<T, F> = this.ranker,
        population: Population<T, F> = this.population,
    ) = EvolutionState(generation, ranker, population)

    /**
     * Applies a transformation function to each individual in the population and returns a new `EvolutionState`.
     *
     * This method allows you to modify each individual in the population using the provided transformation function,
     * creating a new `EvolutionState` instance with the resulting transformed population.
     *
     * ## Usage:
     * This method is useful for scenarios where you need to apply a consistent modification to all individuals in the
     * population, such as adjusting their fitness scores, mutating their genes, or applying other evolutionary
     * operations.
     *
     * ### Example:
     * ```
     * val newState = currentState.map { individual ->
     *     individual.copy(fitness = individual.fitness + 1)
     * }
     * ```
     * In this example, the `map` function is used to increase the fitness score of each individual in the population by
     * 1.
     *
     * @param function The transformation function to apply to each individual.
     * @return A new instance of `EvolutionState` with the transformed population.
     */
    fun map(function: (Individual<T, F>) -> Individual<T, F>) = copy(population = population.map(function))

    /**
     * Returns the generation component of the `EvolutionState`.
     *
     * @return The current generation number.
     */
    operator fun component1() = generation

    /**
     * Returns the population component of the `EvolutionState`.
     *
     * @return The current population.
     */
    operator fun component2() = population

    /**
     * Returns a string representation of the `EvolutionState`.
     *
     * @return A string representation of the `EvolutionState`.
     */
    override fun toString() = "EvolutionState(generation=$generation, population=$population)"

    /**
     * Checks if the given object is equal to this `EvolutionState`.
     *
     * @param other The object to compare with.
     * @return `true` if the objects are equal, `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EvolutionState<*, *> -> false
        else -> population == other.population && generation == other.generation
    }

    /**
     * Returns the hash code of the `EvolutionState`.
     *
     * @return The hash code of the `EvolutionState`.
     */
    override fun hashCode() = hash(EvolutionState::class, population, generation)

    companion object {
        /**
         * Creates an empty `EvolutionState` with the specified ranker and an initial generation of 0.
         *
         * @param ranker The ranker used to evaluate individuals in the population.
         * @return An empty instance of `EvolutionState`.
         */
        fun <T, G> empty(ranker: IndividualRanker<T, G>) where G : Gene<T, G> = EvolutionState(0, ranker)
    }
}
