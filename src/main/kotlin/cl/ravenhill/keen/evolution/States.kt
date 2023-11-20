/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution


import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import java.util.*

/***************************************************************************************************
 * This code defines two classes: EvolutionResult and EvolutionStart that represent the result of an
 * evolution process and the starting point for a new generation of evolution, respectively.
 * EvolutionResult contains properties for the optimization strategy used, the population of
 * individuals, the generation number, and the best individual of the result.
 * It also has a function to return a new EvolutionStart object for the next generation.
 * EvolutionStart contains properties for the initial population of individuals, the generation
 * number, and a flag indicating whether the evaluation process needs to be run again.
 * It also has a function to create an empty EvolutionStart object.
 * Both classes have generic types DNA and T respectively, which represent the type of the gene's
 * value and the type of the individual.
 **************************************************************************************************/

/**
 * Represents the outcome of an evolutionary generation, encapsulating the population's state
 * at that point along with the optimizer used for fitness comparison.
 *
 * This class is a snapshot of the evolutionary process, providing access to the best individual
 * in the population and enabling transitions to subsequent generations.
 *
 * @param DNA The type of genetic data or information.
 * @param G The gene type, which contains [DNA] type data and conforms to [Gene].
 * @property optimizer The optimization strategy used to assess and rank individuals based on fitness.
 * @property population The collection of individuals at the current generation.
 * @property generation The generation number of the current evolutionary state.
 * @property best Retrieves the fittest individual in the current population using the optimizer's comparator.
 *
 * @constructor Creates an [EvolutionResult] instance with the given optimizer, population, and generation.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionResult<DNA, G : Gene<DNA, G>>(
    val optimizer: IndividualOptimizer<DNA, G>,
    override val population: Population<DNA, G>,
    override val generation: Int
) : EvolutionState<DNA, G>(generation, population) {

    val best: Individual<DNA, G> by lazy {
        constraints { "Cannot get the best individual of an empty population" { population mustNot BeEmpty } }
        population.maxWith(optimizer.comparator)
    }

    /**
     * Applies a transformation function to each individual in the population and creates a new
     * [EvolutionResult] with the resulting population.
     *
     * @param function A transform function to apply to each individual.
     * @return A new [EvolutionResult] with the transformed population.
     */
    fun map(function: (Individual<DNA, G>) -> Individual<DNA, G>) =
        EvolutionResult(optimizer, population.map(function), generation)
}

/**
 * Represents the state of an evolutionary process at a specific point in time.
 *
 * This class encapsulates the current population of individuals and the generation number, providing
 * a snapshot of the evolutionary process. It is used to track the progress and state of a genetic algorithm
 * throughout its execution.
 *
 * ## Constraints:
 * - The generation number must be non-negative. This constraint ensures that the evolutionary process
 *   is logically consistent, as generations cannot be negative.
 *
 * ## Usage:
 * An `EvolutionState` instance is typically created and updated by the [Engine] class during the
 * evolutionary process. It is passed to various components of the genetic algorithm, such as
 * [EvolutionListener]s, to provide context and information about the current state of the evolution.
 *
 * ### Example:
 * ```kotlin
 * val initialPopulation = //... create initial population
 * var currentState = EvolutionState(initialPopulation, 0)
 *
 * // In each iteration of the genetic algorithm:
 * currentState = currentState.next() // Advances to the next generation
 * ```
 *
 * @param DNA The type of data that represents an individual's genotype.
 * @param G The specific type of [Gene] that the individuals in the population possess.
 *
 * @constructor Creates an [EvolutionState] with the provided population and generation number.
 *
 * @property population A list of individuals representing the current population in this state.
 * @property generation The current generation number in the evolutionary process.
 * @property size The number of individuals in the population.
 *
 * @see Individual
 * @see Engine
 * @author <https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
open class EvolutionState<DNA, G : Gene<DNA, G>>(
    open val generation: Int,
    open val population: Population<DNA, G>
) {

    init {
        constraints { "Generation [$generation] must be non-negative" { generation mustNot BeNegative } }
    }

    /**
     * Secondary constructor for [EvolutionState] which allows initializing the state with a variable
     * number of [Individual] objects.
     *
     * This constructor is useful for quickly setting up an [EvolutionState] with a predefined set of individuals,
     * especially in testing or small-scale simulations where it's more convenient to specify individuals directly.
     *
     * @param generation The current generation number. Must be non-negative.
     * @param individuals A variable number of [Individual] objects constituting the population.
     */
    constructor(generation: Int, vararg individuals: Individual<DNA, G>) : this(generation, individuals.toList())

    val size get() = population.size

    /**
     * Advances this state to the next generation.
     *
     * @return A new [EvolutionState] with the same population and incremented generation number.
     */
    operator fun next() = EvolutionState(generation + 1, population)

    /**
     * Provides a destructuring declaration for the population and generation components of this state.
     *
     * @return The population component of this state.
     */
    operator fun component1() = population

    /**
     * Provides a destructuring declaration for the generation component of this state.
     *
     * @return The generation component of this state.
     */
    operator fun component2() = generation

    override fun toString() = "EvolutionState(generation=$generation, population=$population)"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EvolutionState<*, *> -> false
        else -> population == other.population && generation == other.generation
    }

    override fun hashCode() = Objects.hash(EvolutionState::class, population, generation)

    companion object {
        /**
         * Creates an empty [EvolutionState] with no individuals and a generation number of 0.
         *
         * This function can be used to initialize the state of an evolutionary algorithm
         * at the very beginning before any evolution has taken place.
         *
         * @return An empty [EvolutionState].
         */
        fun <DNA, G : Gene<DNA, G>> empty(): EvolutionState<DNA, G> = EvolutionState(0, listOf())
    }
}
