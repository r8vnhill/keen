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
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.MeanCrossover
import cl.ravenhill.keen.util.Minimizer
import kotlin.math.cos
import kotlin.math.sin


/**
 * The function to minimize.
 */
fun fitnessFunction(x: Genotype<Double>): Double {
    val value = x.chromosomes.first().genes.first().dna
    return cos(0.5 + sin(value)) * cos(value)
}

/**
 * Calculates the minimum of the real function:
 * ```
 * f(x) = cos(1 / 2 + sin(x)) * cos(x)
 * ```
 */
fun main() {
    val engine = engine(::fitnessFunction) {
        genotype = genotype {
            chromosomes = listOf(DoubleChromosome.Builder(1, 0.0..(2 * Math.PI)))
        }
        populationSize = 500
        optimizer = Minimizer()
        survivors = (populationSize * 0.2).toInt()
        alterers = listOf(Mutator(0.03), MeanCrossover(0.6))
    }
    engine.evolve()
    engine.statistics.forEach {
        println(it)
    }
}