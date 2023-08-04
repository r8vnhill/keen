/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.examples.ga.optimization

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.doubles
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.combination.MeanCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionPrinter
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

private fun ackley(genotype: Genotype<Double, DoubleGene>): Double {
    val (x, y) = genotype.flatten()
    return -20 * exp(-0.2 * sqrt(0.5 * (x.pow(2) + y.pow(2)))) -
            exp(0.5 * (cos(2 * PI * x) + cos(2 * PI * y))) + exp(1.0) + 20.0
}

fun main() {
    val engine = engine(::ackley, genotype {
        chromosome {
            doubles {
                size = 2
                range = -5.0 to 5.0
            }
        }
    }) {
        populationSize = 500
        optimizer = FitnessMinimizer()
        alterers = listOf(
            Mutator(0.03),
            MeanCrossover(0.3, geneRate = 0.5)
        )
        limits = listOf(SteadyGenerations(100))
        listeners = listOf(
            EvolutionPlotter(),
            EvolutionPrinter(10),
            EvolutionSummary()
        )
    }
    engine.evolve()
    println(engine.listeners.last())
    (engine.listeners.first() as EvolutionPlotter).displayFitness()
}