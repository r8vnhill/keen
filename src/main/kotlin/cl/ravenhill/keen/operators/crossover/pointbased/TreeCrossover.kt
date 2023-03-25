/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.operators.crossover.AbstractCrossover
import cl.ravenhill.keen.util.Tree


class TreeCrossover<DNA>(probability: Double) : AbstractCrossover<Tree<DNA>>(probability) {

//    override fun crossover(
//        genes1: MutableList<Gene<Tree<DNA>>>,
//        genes2: MutableList<Gene<Tree<DNA>>>
//    ): Int {
//        TODO("Not yet implemented")
//    }

    override fun crossoverChromosomes(chromosomes: List<Chromosome<Tree<DNA>>>): List<Chromosome<Tree<DNA>>> {
        TODO("Not yet implemented")
    }
}