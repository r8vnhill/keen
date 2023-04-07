package cl.ravenhill.keen.problems.ga.basics

import cl.ravenhill.keen.builders.booleans
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter


/**
 * Counts the number of `true` values in the given [genotype].
 *
 * @param genotype The genotype to count the `true` values in.
 * @return The number of `true` values in the given [genotype] as a [Double].
 */
fun count(genotype: Genotype<Boolean>) = genotype.flatten().count { it }.toDouble()

/**
 * This is an example of using a genetic algorithm to evolve a population of `Boolean` genotypes
 * that contains `true` values with a probability of 0.15. The aim is to evolve a genotype
 * that has at least 20 `true` values.
 *
 * This method creates a new genetic algorithm engine with a custom fitness function that uses the
 * [count] method to evaluate the fitness of each genotype.
 * The engine is then configured with several operators including mutation, crossover, and
 * tournament selection.
 * The engine also has a generation count and a target fitness limit, as well as several statistics
 * collectors, including a [StatisticCollector], [StatisticPrinter], and [StatisticPlotter].
 *
 * Finally, the engine is run and the statistics are printed to the console. The [StatisticPlotter]
 * is used to display a graph of the fitness values over time.
 */
fun main() {
    val engine = engine(::count,
        genotype {
            chromosome {
                booleans { size = 20; truesProbability = 0.15 }
            }
        }) {
        populationSize = 500
        selector = TournamentSelector(sampleSize = 2)
        alterers =
            listOf(Mutator(probability = 0.55), SinglePointCrossover(probability = 0.2))
        limits = listOf(GenerationCount(100), TargetFitness(20.0))
        statistics =
            listOf(StatisticCollector(), StatisticPrinter(1), StatisticPlotter())
    }
    engine.run()
    println(engine.statistics.first())
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}

