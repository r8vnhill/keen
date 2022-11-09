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
import cl.ravenhill.keen.core.chromosomes.CharChromosome
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover


/**
 * Documentation
 */
fun fitnessFn(genotype: Genotype<Char>): Double {
    val target = "Sopaipilla"
    var fitness = 0.0
    genotype.chromosomes.forEach { chromosome ->
        chromosome.genes.forEachIndexed { idx, gene ->
            if (gene.dna == target[idx]) {
                fitness++
            }
        }
    }
    return fitness
}

fun main() {
    val engine = engine(::fitnessFn) {
        genotype = genotype {
            chromosomes = listOf(CharChromosome.Builder(10))
        }
        populationSize = 1000
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(10))
    }
    engine.evolve()
    println(engine.statistics)
}