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
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer

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
data class EvolutionResult<DNA, G : Gene<DNA, G>>(
    val optimizer: IndividualOptimizer<DNA, G>,
    override val population: Population<DNA, G>,
    override val generation: Int
) : EvolutionState<DNA, G>(population, generation) {

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
 * Represents the starting point for a new generation of evolution.
 *
 * @property population The initial population of individuals.
 * @property generation The generation number.
 *
 * @param DNA The type of the individual.
 *
 * @constructor Creates a new [EvolutionState] object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
open class EvolutionState<DNA, G : Gene<DNA, G>>(
    open val population: List<Individual<DNA, G>>,
    open val generation: Int
) {

    init {
        constraints { "Generation [$generation] must be non-negative" { generation mustNot BeNegative } }
    }


    /**
     * Advances the evolution result to the next generation, incrementing the generation count.
     *
     * @return An [EvolutionState] representing the next stage in the evolutionary process.
     */
    operator fun next() = EvolutionState(population, generation + 1)

    companion object {
        /**
         * Creates an empty [EvolutionState] object.
         *
         * @param DNA The type of the individual.
         *
         * @return An empty [EvolutionState] object.
         */
        fun <DNA, G : Gene<DNA, G>> empty(): EvolutionState<DNA, G> = EvolutionState(listOf(), 0)
    }
}
