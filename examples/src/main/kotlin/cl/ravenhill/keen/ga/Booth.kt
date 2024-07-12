/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.doubles
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.AverageCrossover
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import cl.ravenhill.keen.ranking.FitnessMinRanker
import kotlin.math.pow

fun booth(gt: Genotype<Double, DoubleGene>) = gt.flatten().let { (x, y) ->
    (x + 2 * y - 7).pow(2) + (2 * x + y - 5).pow(2)
}

fun main() {
    val engine = evolutionEngine(::schaffer2, genotypeOf {
        chromosomeOf {
            doubles {
                ranges += -10.0..10.0
                size = 2
            }
        }
    }) {
        ranker = FitnessMinRanker()
        populationSize = 500
        parentSelector = RouletteWheelSelector()
        survivorSelector = RouletteWheelSelector()
        alterers += listOf(RandomMutator(0.1), AverageCrossover(0.3))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
        limits += listOf(SteadyGenerations(50), MaxGenerations(500))
    }
    engine.evolve()
    engine.listeners.forEach { it.display() }
}