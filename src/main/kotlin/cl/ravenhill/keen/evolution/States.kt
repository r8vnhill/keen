/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution


import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
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
 * Result of an evolution process.
 *
 * @param DNA The type of the gene's value.
 *
 * @property optimizer The optimization strategy used.
 * @property population The population of the result.
 * @property generation The generation of the result.
 * @property best The best individual of the result.
 *
 * @constructor Creates a new [EvolutionResult] with the given [optimizer], [population], and
 *  [generation].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionResult<DNA, G : Gene<DNA, G>>(
    val optimizer: IndividualOptimizer<DNA, G>,
    val population: Population<DNA, G>,
    val generation: Int
) : Comparable<EvolutionResult<DNA, G>> {

    val best: Individual<DNA, G>
        get() = population.maxWith(optimizer.comparator)

    /**
     * Returns a new [EvolutionState] object for the next generation.
     */
    operator fun next() = EvolutionState(population, generation + 1, true)

    /**
     * Creates a new [EvolutionResult] with the population transformed by the provided function.
     *
     * @param function A function to transform each individual in the population.
     * @return A new [EvolutionResult] with the transformed population.
     */
    fun map(function: (Individual<DNA, G>) -> Individual<DNA, G>) =
        EvolutionResult(optimizer, population.map(function), generation)

    /// Documentation inherited from [Comparable].
    override fun compareTo(other: EvolutionResult<DNA, G>) =
        optimizer.comparator.compare(this.best, other.best)
    /// Documentation inherited from [Any].
    override fun toString() = "EvolutionResult { generation: $generation, best: $best }"
}

/**
 * Represents the starting point for a new generation of evolution.
 *
 * @property population The initial population of individuals.
 * @property generation The generation number.
 * @property isDirty A flag indicating whether the evaluation process needs to be run again.
 *  The default value is `true`.
 *
 * @param DNA The type of the individual.
 *
 * @constructor Creates a new [EvolutionState] object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionState<DNA, G : Gene<DNA, G>>(
    val population: List<Individual<DNA, G>>,
    val generation: Int,
    val isDirty: Boolean = true
) {

    init {
        constraints { "Generation [$generation] must be non-negative" { generation must BeAtLeast(0) } }
    }

    override fun toString() = "EvolutionStart { " +
            "population: $population, " +
            "generation: $generation, " +
            "isDirty: $isDirty" +
            " }"

    companion object {
        /**
         * Creates an empty [EvolutionState] object.
         *
         * @param DNA The type of the individual.
         *
         * @return An empty [EvolutionState] object.
         */
        fun <DNA, G : Gene<DNA, G>> empty(): EvolutionState<DNA, G> = EvolutionState(listOf(), 1)
    }
}
