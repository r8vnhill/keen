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
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.limits.Match
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPrinter

private const val target = "Sopaipilla"
private fun matches(genotype: Genotype<Char>) = genotype.chromosomes.first().genes
    .filterIndexed { index, gene -> gene.dna == target[index] }
    .size.toDouble()

fun main() {
    val engine = engine(::matches, genotype {
        chromosomes = listOf(CharChromosome.Builder(10))
    }) {
        populationSize = 500
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(Match { bestFitness == 10.0 })
        statistics = listOf(StatisticPrinter(10), StatisticCollector())
    }
    engine.evolve()
    engine.statistics.forEach { println(it) }
}