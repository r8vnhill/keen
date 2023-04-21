/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.problems.ga.knapsack

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import kotlin.math.abs

/**
 * The maximum weight that the knapsack can hold.
 */
private const val MAX_WEIGHT = 15

/**
 * The possible items that can be put in the knapsack.
 */
private val items = listOf(4 to 12, 2 to 1, 2 to 2, 1 to 1, 10 to 4, 0 to 0)

/**
 * The fitness function for the knapsack problem.
 * It calculates the fitness of a given genotype by summing the values of the items in the knapsack.
 * If the weight of the knapsack is greater than the maximum weight, the fitness is reduced by the
 * difference between the weight and the maximum weight.
 *
 * @param genotype The genotype to calculate the fitness for.
 * @return The fitness of the genotype.
 */
private fun fitnessFn(genotype: Genotype<Pair<Int, Int>, KnapsackGene>): Double {
    val items = genotype.flatten()
    val value = items.sumOf { it.first }
    val weight = items.sumOf { it.second }
    return value - if (!genotype.verify()) {
        abs(MAX_WEIGHT - weight).toDouble() * 50
    } else 0.0
}

/**
 * A gene implementation for the knapsack problem, where each gene represents a pair of
 * integers (value, weight).
 *
 * @property dna The pair of integers represented by this gene.
 */
class KnapsackGene(override val dna: Pair<Int, Int>) : Gene<Pair<Int, Int>, KnapsackGene> {
    // \ Documentation inherited from [Gene]
    override fun generator() = items.random(Core.random)

    /// Documentation inherited from [Gene]
    override fun withDna(dna: Pair<Int, Int>) = KnapsackGene(dna)

    /// Documentation inherited from [Any]
    override fun toString() = "(${dna.first}, ${dna.second})"
}

/**
 * A chromosome representing a solution to the Knapsack problem.
 *
 * @property genes The list of genes making up the chromosome.
 */
class KnapsackChromosome(override val genes: List<KnapsackGene>) :
        Chromosome<Pair<Int, Int>, KnapsackGene> {

    /// Documentation inherited from [Chromosome]
    override fun withGenes(genes: List<KnapsackGene>) =
        KnapsackChromosome(genes.map { KnapsackGene((it.dna)) })

    /**
     * Verifies whether this chromosome satisfies the constraints of the Knapsack problem.
     */
    override fun verify() = genes.sumOf { it.dna.second } <= MAX_WEIGHT

    /// Documentation inherited from [Any]
    override fun toString() = genes.joinToString(", ", "[", "]")

    /**
     * A factory for creating instances of [KnapsackChromosome].
     *
     * @property size The number of genes to include in each chromosome.
     * @property geneFactory The factory function for creating genes to use in the chromosome.
     */
    class Factory(override var size: Int, private val geneFactory: () -> KnapsackGene) :
            Chromosome.AbstractFactory<Pair<Int, Int>, KnapsackGene>() {
        /// Documentation inherited from [Chromosome.Factory]
        override fun make() = KnapsackChromosome((0 until size).map { geneFactory() })
    }
}

/**
 * The unbounded knapsack problem is a well-known problem in computer science that involves filling
 * a knapsack with a maximum weight capacity with items that have different weights and values.
 * The goal is to maximize the total value of the items in the knapsack without exceeding the
 * maximum weight capacity.
 *
 * This problem is NP-complete, meaning that it's unlikely that an efficient algorithm exists for
 * solving it exactly in polynomial time.
 * Instead, we must rely on heuristic approaches that provide approximate solutions.
 *
 * This code uses a genetic algorithm to solve the unbounded knapsack problem.
 * The approach involves representing each item as a pair of integers (value, weight) and using a
 * chromosome to represent a solution to the problem.
 * In this implementation, the chromosome is composed of genes that represent individual items.
 *
 * The fitness function for the problem calculates the fitness of a given genotype by summing the
 * values of the items in the knapsack.
 * If the weight of the knapsack is greater than the maximum weight, the fitness is reduced by the
 * difference between the weight and the maximum weight.
 *
 * The genetic algorithm is initialized with a population of chromosomes, where each chromosome is
 * created using a factory function that generates random genes representing items.
 * The algorithm uses a combination of mutation and crossover to generate new solutions, with the
 * aim of converging to the optimal solution over time.
 * Finally, the algorithm returns the best solution found.
 *
 * Overall, this code provides a good illustration of how genetic algorithms can be used to solve
 * NP-complete problems like the unbounded knapsack problem.
 * However, the efficiency of the algorithm is dependent on the number of iterations and the size of
 * the population, which can make it slow for large problem sizes.
 */
fun main() {
    val engine = engine(::fitnessFn, genotype {
        chromosome {
            KnapsackChromosome.Factory(15) { KnapsackGene(items.random(Core.random)) }
        }
    }) {
        populationSize = 100
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.2))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
        statistics = listOf(StatisticPrinter(1), StatisticPlotter(), StatisticCollector())
    }
    val result = engine.evolve()
    println(engine.statistics.last())
    println(result.best.flatten().filter { it.first != 0 })
    (engine.statistics[1] as StatisticPlotter).displayFitness()
}