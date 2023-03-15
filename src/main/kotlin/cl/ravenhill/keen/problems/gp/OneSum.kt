package cl.ravenhill.keen.problems.gp

import cl.ravenhill.keen.Builders.Chromosomes.program
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.evolution.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.SingleNodeCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.functions.Add
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.util.logging.Level
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import kotlin.math.abs


private fun fitness(target: Int): (Genotype<Reduceable<Double>>) -> Double = { gt ->
    abs(target - gt.flatten().first()(arrayOf()))
}

fun main() {
//    Core.EvolutionLogger.level = Level.Debug()
    Core.maxProgramDepth = 10
    val engine = engine(fitness(20), genotype {
        chromosome {
            program {
                function { Add() }
                terminal { EphemeralConstant { 1.0 } }
            }
        }
    }) {
        populationSize = 100
        limits = listOf(TargetFitness(0.0), GenerationCount(1000))
        alterers = listOf(Mutator(0.03), SingleNodeCrossover(0.2))
        optimizer = FitnessMinimizer()
        statistics =
            listOf(StatisticCollector(), StatisticPrinter(10), StatisticPlotter())
        evaluator = SequentialEvaluator(fitness(20))
    }
    val result = engine.run()
    println(engine.statistics.first())
    println(result)
    (engine.statistics.last() as StatisticPlotter).displayFitness()
}
