/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker
import java.util.*


open class EvolutionState<T, F>(
    val generation: Int,
    val ranker: IndividualRanker<T, F>,
    val population: Population<T, F>,
) where F : Feature<T, F> {

    init {
        constraints { "Generation [$generation] must not be negative" { generation mustNot BeNegative } }
    }

    /**
     * Alternative constructor to create `EvolutionState` from a varargs of individuals.
     *
     * @param generation The current generation number.
     * @param ranker The [IndividualRanker] used to rank individuals in the population.
     * @param individuals Varargs of `Individual` objects forming the population.
     */
    constructor(
        generation: Int,
        ranker: IndividualRanker<T, F>,
        vararg individuals: Individual<T, F>,
    ) : this(generation, ranker, individuals.toList())

    /**
     * The size of the population in the current generation.
     */
    val size = population.size

    /**
     * Progresses the evolutionary process to the next generation.
     *
     * This method is pivotal in evolutionary algorithms, marking the transition from one generation to the next.
     * It increments the generation count, signifying an advancement in the evolutionary timeline. The current
     * population is carried over to the next generation, maintaining the continuity of the evolutionary process.
     *
     * The `next()` method effectively creates a new instance of `EvolutionState`, with the generation number
     * incremented by one. This new instance represents the state of the evolutionary process in the subsequent
     * generation. It is crucial for iterative evolutionary algorithms where the state transitions through multiple
     * generations, typically involving selection, reproduction, and mutation processes in each step.
     *
     * ## Example Usage:
     * ```
     * var currentState = EvolutionState(0, initialPopulation, myRanker)
     * for (i in 1..numberOfGenerations) {
     *     // Perform evolutionary operations on currentState's population
     *     currentState = currentState.next() // Transition to the next generation
     * }
     * ```
     * In this example, the evolutionary algorithm iterates over a set number of generations. In each iteration,
     * after performing necessary operations on the current state's population, it advances to the next generation
     * using the `next()` method.
     *
     * @return A new instance of `EvolutionState` representing the state at the next generation, with the generation
     *   number incremented and the current population carried over.
     */
    @Deprecated("Use copy() instead", ReplaceWith("copy(generation = generation + 1)"))
    operator fun next() = EvolutionState(generation + 1, ranker, population)

    /**
     * Returns a boolean indicating whether the population is empty or not.
     *
     * @return `true` if the population is empty, `false` otherwise.
     */
    fun isEmpty() = population.isEmpty()

    /**
     * Constructs a new `EvolutionState` instance, optionally overriding specific attributes.
     *
     * This method provides a way to create a new `EvolutionState` based on the current state, with the ability
     * to modify certain attributes. It is useful in evolutionary algorithms for progressing to the next state or
     * making adjustments to the current state without modifying the original instance, adhering to immutability
     * principles.
     *
     * ## Usage:
     * This method is commonly used in the evolutionary process to create subsequent states, for instance, after
     * applying genetic operations like selection, crossover, and mutation, or when needing to alter the ranker
     * or population while keeping the current generation number.
     *
     * ### Example:
     * ```
     * val currentState = EvolutionState(generation, ranker, population)
     * val nextState = currentState.copy(
     *     generation = currentState.generation + 1,
     *     population = modifiedPopulation
     * )
     * ```
     * In this example, `nextState` represents the state of the next generation. It retains the ranker from
     * `currentState` but updates the generation number and population.
     *
     *
     * @param generation The generation number for the new state. Defaults to the generation number of this instance.
     * @param ranker The individual ranker to be used in the new state. Defaults to the ranker of this instance.
     * @param population The population of individuals for the new state. Defaults to the population of this instance.
     * @return A new `EvolutionState` instance with the specified parameters or defaults from the current state.
     */
    fun copy(
        generation: Int = this.generation,
        ranker: IndividualRanker<T, F> = this.ranker,
        population: Population<T, F> = this.population,
    ) = EvolutionState(generation, ranker, population)

    fun map(function: (Individual<T, F>) -> Individual<T, F>) = copy(population = population.map(function))

    /**
     * Destructuring operator to extract the generation number.
     */
    operator fun component1() = generation

    /**
     * Destructuring operator to extract the population.
     */
    operator fun component2() = population

    /**
     * Provides a comprehensive string representation of the current state of evolution.
     *
     * This method generates a string that encapsulates the key aspects of the `EvolutionState`: the generation
     * number and the population's summary. It presents a snapshot of the evolutionary process at a particular
     * generation, reflecting both the stage of evolution (via the generation number) and the state of the population
     * at that stage. The population is summarized using the simple string representations of the individuals,
     * providing a concise yet informative overview.
     *
     * The string representation generated by this method is particularly useful for logging, debugging, or
     * displaying the state of the evolutionary process in a human-readable format. It enables quick insights
     * into the evolution's progress and the composition of the population at a specific generation.
     *
     * ## Format:
     * The string is formatted as follows:
     * "EvolutionState(generation=<generation_number>, population=[<individual1>, <individual2>, ...])"
     *
     * @return A string that represents the `EvolutionState`, including the generation number and a summary
     *   of the population.
     */
    override fun toString() =
        "EvolutionState(generation=$generation, population=${population.map { it.toSimpleString() }})"


    /**
     * Evaluates equality between this `EvolutionState` and another object.
     *
     * This method is vital for determining whether another object is considered equivalent to this `EvolutionState`.
     * Equality is assessed based on the generation number and the composition of the population. Two `EvolutionState`
     * instances are considered equal if they are at the same generation and have identical populations.
     *
     * ## Important Points:
     * - **Identity Check**: Initially checks if the compared object is the same instance as this one.
     * - **Type Check**: Ensures that the other object is also an instance of `EvolutionState`. If not, they cannot
     *   be equal.
     * - **State Equality**: Compares the generation number and population. Both must match for the objects to be
     *   deemed equal.
     *
     * @param other The object to be compared with this `EvolutionState` for equality.
     * @return `true` if the specified object is an `EvolutionState` at the same generation with an identical
     *   population, `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EvolutionState<*, *> -> false
        else -> population == other.population && generation == other.generation
    }

    /**
     * Generates a hash code for this `EvolutionState`.
     *
     * The hash code is a crucial aspect of data structures like hash tables, where it aids in efficiently locating an
     * object's index. In `EvolutionState`, the hash code is computed by combining the hash codes of the class itself,
     * the population, and the generation number. This approach ensures that different `EvolutionState` instances with
     * distinct populations or generations produce different hash codes, while identical states yield the same hash
     * code.
     *
     * @return An integer representing the hash code of this `EvolutionState`.
     */
    override fun hashCode() = Objects.hash(EvolutionState::class, population, generation)

    /**
     * Companion object for the [EvolutionState] class providing utility functions.
     */
    companion object {
        /**
         * Creates an empty [EvolutionState] object.
         *
         * This function generates an instance of [EvolutionState] representing the initial state in an
         * evolutionary algorithm. It initializes the state with a generation number of 0 and an empty population.
         * This is particularly useful when starting an evolutionary process from scratch, where no individuals
         * have been generated or evaluated yet.
         *
         * ## Usage:
         * This method is commonly used at the beginning of an evolutionary algorithm to set up the initial state.
         * It ensures a clean start with no pre-existing individuals, allowing the algorithm to commence from a
         * baseline generation.
         *
         * ### Example:
         * ```kotlin
         * // Creating an empty initial state for an evolutionary algorithm
         * val initialState: EvolutionState<MyDataType, MyGeneType> = EvolutionState.empty(myRanker)
         * ```
         *
         * In this example, `initialState` is an `EvolutionState` instance ready to be used at the start of an
         * evolutionary process. It has a generation number of 0 and no individuals in its population.
         *
         * @param T The type representing the genetic data in the genes.
         * @param G The type of gene in the evolution state. Must extend [Gene].
         * @param ranker The [IndividualRanker] used to rank individuals in the population.
         * @return An [EvolutionState] object with generation number 0 and an empty population.
         */
        fun <T, G> empty(ranker: IndividualRanker<T, G>) where G : Gene<T, G> = EvolutionState(0, ranker)
    }
}
