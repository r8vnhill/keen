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


class Mutator<DNA>(probability: Double) : AbstractAlterer<DNA>(probability) {
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