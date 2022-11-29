/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.math.toIntProbability
import kotlin.math.pow


/**
 * The mutator operator is responsible for mutating the [Genotype] of the [Individual]s in the
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
class Mutator<DNA>(probability: Double) : AbstractAlterer<DNA>(probability) {

    /**
     * Mutates a population.
     *
     * @param population The population to mutate
     * @param generation The current generation
     * @return The mutated population
     */
    override fun invoke(population: Population<DNA>, generation: Int): AltererResult<DNA> {
        val p = probability.pow(1 / 3.0)
        val widenedProbability = p.toIntProbability()
        val result = population.map {
            if (Core.rng.nextInt() < widenedProbability) {
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
    ) = mutateGenotype(phenotype.genotype, prob).map {
        Phenotype(it, generation)
    }

    private fun mutateGenotype(
        genotype: Genotype<DNA>,
        prob: Double
    ): MutatorResult<Genotype<DNA>> {
        val widenedProbability = prob.toIntProbability()
        val result = genotype.sequence().map {
            if (Core.rng.nextInt() < widenedProbability) {
                mutateChromosome(it, prob)
            } else {
                MutatorResult(it, 0)
            }
        }.toList()
        return MutatorResult(
            genotype.duplicate(result.map { it.result }),
            result.stream()
                .mapToInt { it.mutations }
                .sum()
        )
    }

    private fun mutateChromosome(
        chromosome: Chromosome<DNA>,
        prob: Double
    ): MutatorResult<Chromosome<DNA>> {
        val widenedProbability = prob.toIntProbability()
        val result = chromosome.sequence().map {
            if (Core.rng.nextInt() < widenedProbability) {
                MutatorResult(mutateGene(it), 1)
            } else {
                MutatorResult(it)
            }
        }.toList()
        return MutatorResult(
            chromosome.duplicate(result.map { it.result }),
            result.sumOf { it.mutations }
        )
    }

    private fun mutateGene(gene: Gene<DNA>) = gene.mutate()

    override fun toString() = "Mutator { " +
            "probability: $probability }"
}

data class MutatorResult<T>(val result: T, val mutations: Int = 0) {
    fun <B> map(block: (T) -> B) = MutatorResult(block(result), mutations)
}