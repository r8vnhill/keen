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
import cl.ravenhill.keen.core.chromosomes.DoubleChromosome
import cl.ravenhill.keen.limits.Match
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.MeanCrossover
import cl.ravenhill.keen.util.Minimizer
import java.lang.Math.PI
import kotlin.math.cos

private const val A = 10.0
private const val R = 5.12
private const val N = 2

fun rastriginFunction(x: Genotype<Double>) =
    A * x.chromosomes[0].size + x.chromosomes[0].genes.sumOf { it.dna * it.dna - A * cos(2 * PI * it.dna) }

fun main() {
    val engine = engine(::rastriginFunction) {
        genotype = genotype {
            chromosomes = listOf(DoubleChromosome.Builder(2, -R..R))
            populationSize = 500
            survivors = (500 * 0.2).toInt()
            optimizer = Minimizer()
            alterers = listOf(Mutator(0.03), MeanCrossover(0.6))
            limits = listOf(Match { this.bestFitness < 1e-6 })
        }
    }
    engine.evolve()
    engine.statistics.forEach { println(it) }
}
