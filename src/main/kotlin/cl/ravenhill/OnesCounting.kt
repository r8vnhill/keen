/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.core.Genotype
import cl.ravenhill.keen.core.chromosomes.BoolChromosome
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.util.statistics.StatisticCollector


fun count(genotype: Genotype<Boolean>): Double =
    genotype.chromosomes[0].genes.count { it.dna }.toDouble()

fun main() {
    val engine = engine(::count) {
        genotype = genotype {
            chromosomes = listOf(BoolChromosome.Builder(24, 0.15))
        }
        populationSize = 500
        survivors = (populationSize * 0.2).toInt()
        survivorSelector = RouletteWheelSelector()
        alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
    }
    engine.evolve()
    engine.statistics.forEach {
        println(it)
    }
}
