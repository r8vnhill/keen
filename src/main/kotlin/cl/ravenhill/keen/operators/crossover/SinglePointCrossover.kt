/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Performs a crossover between two genotypes using a random single-point crossover.
 */
class SinglePointCrossover<DNA>(probability: Double) : MultiPointCrossover<DNA>(probability, 1) {

    override fun crossover(mates: Pair<Chromosome<DNA>, Chromosome<DNA>>): Chromosome<DNA> {
        val genes = mutableListOf<Gene<DNA>>()
        val cut = Core.rng.nextInt(mates.first.genes.size)
        genes.addAll(mates.first.genes.take(cut))
        genes.addAll(mates.second.genes.drop(cut))
        return mates.first.copy(genes)
    }

    override fun toString() = "SinglePointCrossover { probability: $probability }"
}