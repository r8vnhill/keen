/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.examples.knapsack

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.Core.rng
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.Mutator
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.util.statistics.StatisticCollector
import kotlin.math.abs
import kotlin.random.asKotlinRandom


private const val MAX_WEIGHT = 15

private val items = listOf(4 to 12, 2 to 1, 2 to 2, 1 to 1, 10 to 4, 0 to 0)

private fun fitnessFn(genotype: Genotype<Pair<Int, Int>>): Double {
    val chromosome = genotype.chromosomes.first()
    val items = chromosome.genes.map { it.dna }
    val value = items.sumOf { it.first }
    val weight = items.sumOf { it.second }
    return value - (if (MAX_WEIGHT < weight) {
        abs(MAX_WEIGHT - weight).toDouble() * 50
    } else 0.0)
}

class KnapsackGene(override val dna: Pair<Int, Int>) : Gene<Pair<Int, Int>> {
    override fun mutate() = KnapsackGene(items.random(rng.asKotlinRandom()))

    override fun duplicate(dna: Pair<Int, Int>) = KnapsackGene(dna)

    override fun toString() = "(${dna.first}, ${dna.second})"
}

class KnapsackChromosome(override val genes: List<KnapsackGene>) : Chromosome<Pair<Int, Int>> {
    override fun duplicate(genes: List<Gene<Pair<Int, Int>>>) =
        KnapsackChromosome(genes.map { KnapsackGene((it.dna)) })

    override fun verify() = genes.sumOf { it.dna.second } <= MAX_WEIGHT

    override fun toString() = genes.joinToString(", ", "[", "]")

    class Factory(private val size: Int, private val geneFactory: () -> KnapsackGene) :
        Chromosome.Factory<Pair<Int, Int>> {
        override fun make() = KnapsackChromosome((0 until size).map { geneFactory() })
    }
}

fun main() {
    val engine = engine(::fitnessFn, genotype {
        chromosomes =
            listOf(KnapsackChromosome.Factory(15) { KnapsackGene(items.random(rng.asKotlinRandom())) })
    }) {
        populationSize = 20000
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
        statistics = listOf(/*StatisticPrinter(1),*/ StatisticCollector())
    }
    val result = engine.run()
    println(engine.statistics.last())
    println(result.best?.genotype?.toDNA()?.first()?.filter { it.first != 0 })
}