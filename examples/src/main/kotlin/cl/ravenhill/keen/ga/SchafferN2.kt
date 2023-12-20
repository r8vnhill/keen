/*
 * Copyright (c) 2023, Ignacio Slater M.
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
import cl.ravenhill.keen.listeners.EvolutionPlotter
import cl.ravenhill.keen.listeners.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.AverageCrossover
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.operators.selection.RandomSelector
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMinRanker
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun schaffer2(gt: Genotype<Double, DoubleGene>) = gt.flatten().let { (x, y) ->
    val a = sin(sqrt(x.pow(2) + y.pow(2))).pow(2) - 0.5
    val b = 1 + 0.001 * (x.pow(2) + y.pow(2)).pow(2)
    0.5 + a / b
}

fun main() {
    val engine = evolutionEngine(::schaffer2, genotypeOf {
        chromosomeOf {
            doubles {
                ranges += -100.0..100.0
                size = 2
            }
        }
    }) {
        ranker = FitnessMinRanker()
        populationSize = 500
        parentSelector = TournamentSelector()
        survivorSelector = TournamentSelector()
        alterers += listOf(RandomMutator(0.1), AverageCrossover(0.3))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
        limits += listOf(SteadyGenerations(50), MaxGenerations(500))
    }
    engine.evolve()
    engine.listeners.forEach { it.display() }
}
