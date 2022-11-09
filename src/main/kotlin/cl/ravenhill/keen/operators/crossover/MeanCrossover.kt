/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.core.chromosomes.Chromosome
import cl.ravenhill.keen.core.genes.Gene


/**
 * Performs a crossover between two genotypes using the mean of the genes.
 *
 * @param DNA   The type of the DNA.
 *              Must be a [Number] type.
 * @param probability   The probability of this crossover to be applied.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class MeanCrossover<DNA : Number>(probability: Double) : AbstractCrossover<DNA>(probability) {

    override fun crossover(mates: Pair<Chromosome<DNA>, Chromosome<DNA>>): Chromosome<DNA> {
        val genes = mutableListOf<Gene<DNA>>()
        for (i in mates.first.genes.indices) {
            crossover(mates.first.genes[i] to mates.second.genes[i]).let { genes.add(it) }
        }
        return mates.first.copy(genes)
    }

    private fun crossover(genes: Pair<Gene<DNA>, Gene<DNA>>): Gene<DNA> {
        @Suppress("UNCHECKED_CAST")
        return genes.first.copy(
            if (KeenCore.generator.nextDouble() < probability) {
                (genes.first.dna.toDouble() + genes.second.dna.toDouble()) / 2
            } else {
                genes.first.dna
            } as DNA
        )
    }

    override fun toString() = "MeanCrossover { probability: $probability }"
}