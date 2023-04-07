package cl.ravenhill.keen.problems.gp

import cl.ravenhill.keen.Builders.Chromosomes.program
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SingleNodeCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import kotlin.math.abs


/**
 * Returns a fitness function that computes the absolute difference between the sum of the
 * generated program and a target integer value.
 *
 * @param target the integer value to which the sum of the generated program will be compared.
 * @return a fitness function that computes the absolute difference between the sum of the
 *  generated program and the target integer value.
 */
private fun fitness(target: Int): (Genotype<Program<Double>>) -> Double = { gt ->
    val program = gt.flatten().first()
    abs(program() - target)
}

/**
 * Finds a program that can generate a target number by adding ones.
 * The fitness function evaluates the difference between the program's output and the target number.
 * The genetic algorithm's goal is to minimize the fitness function.
 * The algorithm uses a combination of mutation and single-node crossover as alterers.
 * The algorithm runs for a maximum of 1000 generations or until a program with zero fitness is
 * found.
 * The final result is displayed along with statistics and a fitness plot.
 */
fun main() {
    // Set up the genetic algorithm engine
    val engine = engine(fitness(20), genotype {
        chromosome {
            program {
                function("+", 2) { it[0] + it[1] }
                terminal { EphemeralConstant { 1.0 } }
            }
        }
    }) {
        populationSize = 100
        limits = listOf(TargetFitness(0.0), GenerationCount(1000))
        alterers = listOf(Mutator(0.06), SingleNodeCrossover(0.2))
        optimizer = FitnessMinimizer()
        statistics =
            listOf(StatisticCollector(), StatisticPrinter(10), StatisticPlotter())
    }
    // Run the genetic algorithm and display results
    val result = engine.run()
    println(engine.statistics.first())
    println(result)
    (engine.statistics.last() as StatisticPlotter).displayFitness()// { if (it == 0.0) 0.0 else ln(it) }
}
