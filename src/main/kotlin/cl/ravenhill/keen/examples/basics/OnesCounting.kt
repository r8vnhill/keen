/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.examples.basics

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.statistics.StatisticCollector


fun count(genotype: Genotype<Boolean>) = genotype.flatten().count { it }.toDouble()

/**
 * See the wiki for more information about this example.
 */
fun main() {
    val engine = engine(::count, genotype {
        chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
    }) {
        populationSize = 500
        selector = TournamentSelector(sampleSize = 2)
        alterers = listOf(Mutator(probability = 0.55), SinglePointCrossover(probability = 0.06))
        limits = listOf(GenerationCount(100), TargetFitness(20.0))
        statistics = listOf(StatisticCollector())
    }
    engine.run()
    println(engine.statistics[0])
}
