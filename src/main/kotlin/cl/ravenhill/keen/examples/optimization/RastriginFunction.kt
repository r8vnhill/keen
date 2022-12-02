/*
 *
 *  * "Keen" (c) by R8V.
 *  * "Keen" is licensed under a
 *  * Creative Commons Attribution 4.0 International License.
 *  * You should have received a copy of the license along with this
 *  *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 *
 */

package cl.ravenhill.keen.examples.optimization

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.MeanCrossover
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import kotlin.math.cos

private const val A = 10.0
private const val R = 5.12
private const val N = 2

private fun fitness(x: Genotype<Double>): Double {
    return A * N + x.chromosomes.first().genes.fold(0.0) { acc, gene ->
        acc + gene.dna * gene.dna - A * cos(2 * Math.PI * gene.dna)
    }
}

fun main() {
    val engine = engine(::fitness, genotype {
        chromosomes = listOf(DoubleChromosome.Builder(2, -R..R))
    }) {
        populationSize = 1000
        optimizer = FitnessMinimizer()
        alterers = listOf(Mutator(0.1), MeanCrossover(0.6))
        limits = listOf(SteadyGenerations(20))
        statistics = listOf(StatisticCollector())
    }
    engine.run()
    println(engine.statistics[0])
}