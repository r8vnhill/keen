package cl.ravenhill.keen.examples.basics

import cl.ravenhill.keen.Builders.Chromosomes.bool
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter


fun count(genotype: Genotype<Boolean>) = genotype.flatten().count { it }.toDouble()

/**
 * See the wiki for more information about this example.
 */
fun main() {
    val engine = engine(::count, genotype {
        chromosome { bool(20, 0.15) }
    }) {
        populationSize = 500
        selector = TournamentSelector(sampleSize = 2)
        alterers = listOf(Mutator(probability = 0.55), SinglePointCrossover(probability = 0.2))
        limits = listOf(GenerationCount(100), TargetFitness(20.0))
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    engine.run()
    println(engine.statistics.first())
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}
