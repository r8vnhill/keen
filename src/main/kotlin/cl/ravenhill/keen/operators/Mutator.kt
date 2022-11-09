/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.core.Genotype
import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.core.chromosomes.Chromosome
import cl.ravenhill.keen.core.genes.Gene


class Mutator<DNA>(override val probability: Double) : Alterer<DNA> {
    override fun invoke(population: List<Genotype<DNA>>): List<Genotype<DNA>> {
        return population.map { genotype ->
            val chromosomes = mutableListOf<Chromosome<DNA>>()
            genotype.chromosomes.forEach { chromosome ->
                val genes = mutableListOf<Gene<DNA>>()
                chromosome.genes.forEach { gene ->
                    genes.add(
                        if (KeenCore.generator.nextDouble() < probability) {
                            gene.mutate()
                        } else {
                            gene
                        }
                    )
                }
                chromosomes.add(chromosome.copy(genes))
            }
            genotype.copy(chromosomes)
        }
    }

    override fun toString() = "Mutator { " +
            "probability: $probability }"
}