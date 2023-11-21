/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.doubles
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.combination.AverageCrossover
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionPrinter
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer

/**
 * Creates and returns an evolutionary computation engine for optimizing a given fitness function.
 *
 * The engine is configured to operate on a genotype with a double chromosome of size 2 and within
 * a specified range. The engine uses a population size of 500 and aims to minimize the fitness value.
 * A combination of mutation and crossover operators is used for generating offspring.
 * The engine terminates if the fitness doesn't improve for 100 consecutive generations.
 * Various listeners are added to the engine to facilitate visualization and logging.
 *
 * ## Examples
 * ### Example 1: Optimizing the Beale function
 * ```
 * val bealeFunction: (Genotype<Double, DoubleGene>) -> Double = { /*...*/ }
 * val engine = createEngine(::bealeFunction, -4.5 to 4.5)
 * ```
 * ### Example 2: Optimizing the Ackley function
 * ```
 * val ackleyFunction: (Genotype<Double, DoubleGene>) -> Double = { /*...*/ }
 * val engine = createEngine(::ackleyFunction, -5.0 to 5.0)
 * ```
 *
 * @param fitnessFunc The fitness function to be optimized.
 * @param range The range within which the double values of the genotype can vary.
 * @return A configured [EvolutionEngine] instance.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
fun createEngine(
    fitnessFunc: (Genotype<Double, DoubleGene>) -> Double,
): EvolutionEngine<Double, DoubleGene> {
    return engine(
        fitnessFunc,
        genotype {
            chromosome {
                doubles {
                    this.size = 2
                }
            }
        }
    ) {
        populationSize = 500
        optimizer = FitnessMinimizer()
        alterers = listOf(
            RandomMutator(0.03),
            AverageCrossover(geneRate = 0.5)
        )
        limits = listOf(SteadyGenerations(100))
        listeners += listOf(
            EvolutionPlotter(),
            EvolutionPrinter(10),
            EvolutionSummary()
        )
    }
}

fun createEngine(
    fitnessFunc: (Genotype<Double, DoubleGene>) -> Double,
    selector: Selector<Double, DoubleGene> = TournamentSelector(3)
) = engine(
    fitnessFunc,
    genotype {
        chromosome {
            doubles {
                this.size = 2
//                this.ranges = ranges.toList()
            }
        }
    }
) {
    populationSize = 500
    optimizer = FitnessMinimizer()
    this.selector = selector
    alterers = listOf(
        RandomMutator(0.03),
        AverageCrossover(geneRate = 0.5)
    )
    limits = listOf(SteadyGenerations(50))
    listeners += listOf(
        EvolutionSummary(),
        EvolutionPlotter(),
    )
}
