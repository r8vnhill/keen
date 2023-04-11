package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer

/***************************************************************************************************
 * This code defines two classes: EvolutionResult and EvolutionStart that represent the result of an
 * evolution process and the starting point for a new generation of evolution, respectively.
 * EvolutionResult contains properties for the optimization strategy used, the population of
 * phenotypes, the generation number, and the best phenotype of the result.
 * It also has a function to return a new EvolutionStart object for the next generation.
 * EvolutionStart contains properties for the initial population of phenotypes, the generation
 * number, and a flag indicating whether the evaluation process needs to be run again.
 * It also has a function to create an empty EvolutionStart object.
 * Both classes have generic types DNA and T respectively, which represent the type of the gene's
 * value and the type of the phenotype.
 **************************************************************************************************/

/**
 * Result of an evolution process.
 *
 * @param DNA The type of the gene's value.
 *
 * @property optimizer The optimization strategy used.
 * @property population The population of the result.
 * @property generation The generation of the result.
 * @property best The best phenotype of the result.
 *
 * @constructor Creates a new [EvolutionResult] with the given [optimizer], [population], and
 *  [generation].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionResult<DNA, G : Gene<DNA, G>>(
    val optimizer: PhenotypeOptimizer<DNA, G>,
    val population: Population<DNA, G>,
    val generation: Int
) : Comparable<EvolutionResult<DNA, G>> {

    val best: Phenotype<DNA, G>
        get() = population.maxWith(optimizer.comparator)

    /**
     * Returns a new [EvolutionStart] object for the next generation.
     */
    operator fun next() = EvolutionStart(population, generation + 1, true)

    override fun compareTo(other: EvolutionResult<DNA, G>): Int =
        optimizer.comparator.compare(this.best, other.best)

    override fun toString() = "EvolutionResult { generation: $generation, best: $best }"
}

/**
 * Represents the starting point for a new generation of evolution.
 *
 * @property population The initial population of phenotypes.
 * @property generation The generation number.
 * @property isDirty A flag indicating whether the evaluation process needs to be run again.
 *  The default value is `true`.
 *
 * @param DNA The type of the phenotype.
 *
 * @constructor Creates a new [EvolutionStart] object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class EvolutionStart<DNA, G : Gene<DNA, G>>(
    val population: List<Phenotype<DNA, G>>,
    val generation: Int,
    val isDirty: Boolean = true
) {

    init {
        enforce { "Generation [$generation] must be non-negative" { generation should BeAtLeast(0) } }
    }

    override fun toString() = "EvolutionStart { " +
            "population: $population, " +
            "generation: $generation, " +
            "isDirty: $isDirty" +
            " }"

    companion object {
        /**
         * Creates an empty [EvolutionStart] object.
         *
         * @param DNA The type of the phenotype.
         *
         * @return An empty [EvolutionStart] object.
         */
        fun <DNA, G : Gene<DNA, G>> empty(): EvolutionStart<DNA, G> = EvolutionStart(listOf(), 1)
    }
}
