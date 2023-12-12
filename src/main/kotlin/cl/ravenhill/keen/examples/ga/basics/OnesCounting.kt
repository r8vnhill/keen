/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.basics

import cl.ravenhill.keen.builders.booleans
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionPrinter


/**
 * Counts the number of `true` values in the given [genotype].
 *
 * @param genotype The genotype to count the `true` values in.
 * @return The number of `true` values in the given [genotype] as a [Double].
 */
fun count(genotype: Genotype<Boolean, BoolGene>) = genotype.flatMap().count { it }.toDouble()

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
 * collectors, including a [EvolutionSummary], [EvolutionPrinter], and [EvolutionPlotter].
 *
 * Finally, the engine is run and the statistics are printed to the console. The [EvolutionPlotter]
 * is used to display a graph of the fitness values over time.
 */
fun run() {
    val engine = engine(::count,
        genotype {
            chromosome {
                booleans { size = 50; trueRate = 0.15 }
            }
        }) {
        populationSize = 50
        selector = TournamentSelector(sampleSize = 2)
        alterers =
            listOf(RandomMutator(probability = 0.03), SinglePointCrossover())
        limits = listOf(GenerationCount(100), TargetFitness(50.0))
        listeners +=
            listOf(EvolutionSummary(), EvolutionPrinter(1), EvolutionPlotter())
    }
    engine.evolve()
    println(engine.listeners.first())
    engine.listeners.filterIsInstance<EvolutionPlotter<*, *>>().first().displayFitness()
}

fun main() {
    run()
}
