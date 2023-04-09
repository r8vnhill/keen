/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.operators.crossover.AbstractCrossover

/**
 * Not yet implemented.
 */
open class MultiPointCrossover<DNA, G: Gene<DNA, G>>(probability: Double, private val cuts: Int) :
        AbstractCrossover<DNA, G>(probability) {

    init {
        enforce { cuts should BeAtLeast(1) { "The crossover must have at least one cut" } }
    }

    override fun crossoverChromosomes(chromosomes: List<Chromosome<DNA, G>>): List<Chromosome<DNA, G>> {
        TODO("Multiple point crossover will be implemented on a future release")
    }
}