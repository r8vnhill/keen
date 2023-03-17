//package cl.ravenhill.keen.problems.gp
//
//import cl.ravenhill.keen.Builders
//import cl.ravenhill.keen.Builders.Chromosomes.program
//import cl.ravenhill.keen.Core
//import cl.ravenhill.keen.genetic.Genotype
//import cl.ravenhill.keen.limits.GenerationCount
//import cl.ravenhill.keen.limits.TargetFitness
//import cl.ravenhill.keen.operators.crossover.SingleNodeCrossover
//import cl.ravenhill.keen.operators.mutator.Mutator
//import cl.ravenhill.keen.prog.Reduceable
//import cl.ravenhill.keen.prog.functions.Mul
//import cl.ravenhill.keen.prog.terminals.EphemeralConstant
//import cl.ravenhill.keen.util.math.eq
//import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
//import cl.ravenhill.keen.util.statistics.StatisticCollector
//import cl.ravenhill.keen.util.statistics.StatisticPlotter
//import kotlin.math.abs
//import kotlin.math.ln
//import kotlin.math.pow
//
//private fun fitness(target: Int): (Genotype<Reduceable<Double>>) -> Double = { gt ->
//    abs(target - gt.flatten().first()(arrayOf()))
//}
//
//fun main() {
//    Core.maxProgramDepth = 10
//    val engine = Builders.engine(fitness(420), Builders.genotype {
//        chromosome {
//            program {
//                function { Mul() }
//                terminal { EphemeralConstant { candidateFactors.random(Core.random) } }
//                terminal { EphemeralConstant { 1.0 } }
//            }
//        }
//    }) {
//        populationSize = 1000
//        limits = listOf(TargetFitness(0.0), GenerationCount(1000))
//        alterers = listOf(Mutator(0.06), SingleNodeCrossover(0.2))
//        optimizer = FitnessMinimizer()
//        statistics = listOf(StatisticCollector(), StatisticPlotter())
//    }
//    val result = engine.run()
//    println(engine.statistics.first())
//    println(result)
//    (engine.statistics.last() as StatisticPlotter).displayFitness { if (it eq 0.0) 0.0 else ln(it) }
//}
//
//private val candidateFactors = listOf(
//    2.0,
//    3.0,
//    5.0,
//    7.0,
//    11.0,
//    13.0,
//)