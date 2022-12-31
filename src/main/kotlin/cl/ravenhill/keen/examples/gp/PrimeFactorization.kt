package cl.ravenhill.keen.examples.gp

import cl.ravenhill.keen.Builders
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.SingleNodeCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Add
import cl.ravenhill.keen.prog.functions.Mul
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import kotlin.math.abs

private fun fitness(target: Int): (Genotype<Reduceable<Double>>) -> Double = { gt ->
    abs(target - gt.flatten().first()(arrayOf()))
}

fun main() {
    Core.maxProgramDepth = 3
    val engine = Builders.engine(fitness(420), Builders.genotype {
        chromosome {
            Builders.Chromosomes.program {
                function { Mul() }
                terminal { EphemeralConstant { candidateFactors.random(Core.random) } }
            }
        }
    }) {
        populationSize = 100
        limits = listOf(GenerationCount(100))
        alterers = listOf(Mutator(0.1), SingleNodeCrossover(0.4))
        optimizer = FitnessMinimizer()
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    val result = engine.run()
    println(engine.statistics.first())
    println(result)
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}

private val candidateFactors = listOf(
    1.0,
    2.0,
    3.0,
    5.0,
    7.0,
    11.0,
    13.0,
    17.0,
    23.0,
    27.0,
    29.0,
    31.0,
    37.0,
    41.0,
    43.0,
    47.0,
    53.0,
    59.0,
    61.0,
    67.0,
    71.0,
    73.0,
    79.0,
    83.0,
    89.0,
    97.0,
    101.0,
    103.0,
    107.0,
    109.0,
    113.0,
    127.0,
    131.0,
    137.0,
    139.0,
    149.0,
    151.0,
    157.0,
    163.0,
    167.0,
    173.0,
    179.0,
    181.0,
    191.0,
    193.0,
    197.0,
    199.0
)