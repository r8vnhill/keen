package cl.ravenhill.keen.problems.gp

import cl.ravenhill.keen.Builders
import cl.ravenhill.keen.Builders.Chromosomes.program
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SingleNodeCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import kotlin.math.ln
import kotlin.math.pow

private fun fitness(inputs: List<Double>): (Genotype<Program<Double>>) -> Double = { gt ->
    val program = gt.flatten().first()
    inputs.map { input ->
        val expected = input.pow(4) + input.pow(3) + input.pow(2) + input
        val actual = program(input)
        (expected - actual).pow(2)
    }.average()
}

fun main() {
    Core.maxProgramDepth = 5
    val engine = engine(
        fitness((-10..10).map { it / 10.0 }),
        genotype {
            chromosome {
                program {
                    function("*", 2) { it[0] * it[1] }
                    function("+", 2) { it[0] + it[1] }
                    terminal { EphemeralConstant { Core.random.nextInt(-1, 2).toDouble() } }
                    terminal { Variable("x", 0) }
                }
            }
        }
    ) {
        populationSize = 300
        limits = listOf(TargetFitness(0.0), GenerationCount(1000))
        alterers = listOf(SingleNodeCrossover(0.2), Mutator(0.1))
        optimizer = FitnessMinimizer()
        statistics = listOf(StatisticCollector(), StatisticPlotter())
    }
    val result = engine.run()
    println(engine.statistics.first())
    println(result)
    (engine.statistics.last() as StatisticPlotter).displayFitness { if (it < 0.0) 0.0 else ln(it) }
}