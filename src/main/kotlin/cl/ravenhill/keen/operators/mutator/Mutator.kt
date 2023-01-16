/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.AbstractAlterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.util.math.eq
import cl.ravenhill.keen.util.math.toIntProbability
import kotlin.math.pow


/**
 * The mutator operator is responsible for mutating the [Genotype] of the [Phenotype]s in the
 * [Population].
 * There are two distinct roles for the mutator:
 *
 * - Exploring the search space. This exploration is often slow compared to the crossover,
 * but in problems where crossover is disruptive this can be an important way to explore the
 * landscape.
 *
 * - Maintaining diversity. Mutation prevents the population from correlating.
 * Even if most of the search is done by crossover, mutation is still important to provide the
 * diversity the crossover needs to work.
 *
 * The mutation probability is the value that must be optimized.
 * The optimal value depends on the role mutation plays.
 * If the mutation is the main exploration mechanism, then the mutation probability should be high.
 *
 * @param DNA The type of the DNA
 * @constructor Creates a new [Mutator] with the given [probability]
 */
open class Mutator<DNA>(probability: Double) : AbstractAlterer<DNA>(probability) {

    /**
     * Mutates a population.
     *
     * @param population The population to mutate
     * @param generation The current generation
     * @return The mutated population
     */
    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): AltererResult<DNA> {
        val p = probability.pow(1 / 3.0)
        val widenedProbability = p.toIntProbability()
        val result = population.map {
            if (Core.random.nextInt() < widenedProbability) {
                mutatePhenotype(it, p, generation)
            } else {
                MutatorResult(it)
            }
        }
        return AltererResult(
            result.map { it.result },
            result.stream().mapToInt { it.mutations }.sum()
        )
    }

    private fun mutatePhenotype(
        phenotype: Phenotype<DNA>,
        prob: Double,
        generation: Int
    ) = mutateGenotype(phenotype.genotype).map {
        Phenotype(it, generation)
    }

    private fun mutateGenotype(
        genotype: Genotype<DNA>
    ): MutatorResult<Genotype<DNA>> {
        val result = genotype.chromosomes.map {
            mutateChromosome(it)
        }
        return MutatorResult(
            genotype.duplicate(result.map { it.result }),
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a chromosome and returns a [MutatorResult] with the mutated chromosome and the
     * number of mutations.
     */
    internal open fun mutateChromosome(
        chromosome: Chromosome<DNA>
    ): MutatorResult<Chromosome<DNA>> {
        val result = chromosome.genes.map { mutateGene(it) }
        return MutatorResult(
            chromosome.duplicate(result.map { it.result }),
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a gene and returns a [MutatorResult] with the mutated gene and the number
     * of mutations.
     *
     * The result is defined based in the mutation [probability], the behaviour of this
     * probability can be defined by three cases:
     *
     *  1. The probability is 0.0, then 0 mutations are performed and a copy of the
     *     original gene is returned.
     *  2. The probability is 1.0, then 1 mutation is performed and the mutated gene is
     *     returned.
     *  3. The probability is between 0.0 and 1.0, then a random number is generated and
     *     if it is less than the probability, then 1 mutation is performed and the mutated
     *     gene is returned, otherwise 0 mutations are performed and a copy of the original
     *     gene is returned.
     */
    internal fun mutateGene(gene: Gene<DNA>) =
        if (probability eq 0.0)
            MutatorResult(gene)
        else if (probability eq 1.0 || Dice.probability() < probability)
            MutatorResult(gene.mutate(), 1)
        else MutatorResult(gene)

    override fun toString() = "Mutator { " +
            "probability: $probability }"
}

data class MutatorResult<T>(val result: T, val mutations: Int = 0) {
    fun <B> map(block: (T) -> B) = MutatorResult(block(result), mutations)
}