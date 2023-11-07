/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.benchmarks.optimization

import cl.ravenhill.benchmarks.optimization.functions.ackley
import cl.ravenhill.benchmarks.optimization.functions.beale
import cl.ravenhill.benchmarks.optimization.functions.booth
import cl.ravenhill.benchmarks.optimization.functions.bukinN6
import cl.ravenhill.benchmarks.optimization.functions.crossInTray
import cl.ravenhill.benchmarks.optimization.functions.easom
import cl.ravenhill.benchmarks.optimization.functions.eggholder
import cl.ravenhill.benchmarks.optimization.functions.goldsteinPrice
import cl.ravenhill.benchmarks.optimization.functions.himmelblau
import cl.ravenhill.benchmarks.optimization.functions.holderTable
import cl.ravenhill.benchmarks.optimization.functions.levi
import cl.ravenhill.benchmarks.optimization.functions.matyas
import cl.ravenhill.benchmarks.optimization.functions.mccormick
import cl.ravenhill.benchmarks.optimization.functions.rastrigin
import cl.ravenhill.benchmarks.optimization.functions.rosenbrock
import cl.ravenhill.benchmarks.optimization.functions.schafferN2
import cl.ravenhill.benchmarks.optimization.functions.schafferN4
import cl.ravenhill.benchmarks.optimization.functions.sphere
import cl.ravenhill.benchmarks.optimization.functions.styblinskiTang
import cl.ravenhill.benchmarks.optimization.functions.threeHumpCamel
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.doubles
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.operators.crossover.combination.AverageCrossover
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.operators.selector.RandomSelector
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.utils.DoubleRange
import kotlin.math.abs

object Runner {
    val results = mutableMapOf<String, Pair<MutableList<String>, MutableList<String>>>()

    fun run(
        funName: String,
        function: (Genotype<Double, DoubleGene>) -> Double,
        target: Double,
        selectOp: Selector<Double, DoubleGene>,
        vararg ranges: DoubleRange
    ) {
        var warmup = true
        val initTimes = mutableListOf<Double>()
        val evaluationTimes = mutableListOf<Double>()
        val selectionTimes = mutableListOf<Double>()
        val alterationTimes = mutableListOf<Double>()
        val evolutionTimes = mutableListOf<Double>()
        val totalGenerations = mutableListOf<Double>()
        val fittests = mutableListOf<List<Double>>()
        val errors = mutableListOf<Double>()
        repeat(4) {
            val engine = engine(
                function,
                genotype {
                    chromosome {
                        doubles {
                            this.ranges += ranges
                            this.size = 2
                        }
                    }
                }
            ) {
                optimizer = FitnessMinimizer()
                populationSize = 500
                selector = selectOp
                alterers = listOf(RandomMutator(0.1), AverageCrossover())
                listeners += EvolutionSummary()
                limits = listOf(SteadyGenerations(50))
            }
            val result = engine.evolve()
            if (warmup) {
                warmup = false
            } else {
                with(engine.listeners.first().evolution) {
                    initTimes += initialization.duration / 1000000.0
                    evaluationTimes += generations.map { it.evaluation.duration / 1000000.0 }
                    selectionTimes += generations.map { it.survivorSelection.duration / 1000000.0 }
                    selectionTimes += generations.map { it.offspringSelection.duration / 1000000.0 }
                    alterationTimes += generations.map { it.alteration.duration / 1000000.0 }
                    evolutionTimes += duration / 1000000000.0
                    totalGenerations += generations.size.toDouble()
                    fittests += result.best.flatMap()
                    errors += abs(target - result.best.fitness)
                }
            }
        }
        if (results[selectOp::class.simpleName] == null) {
            results[selectOp::class.simpleName!!] = mutableListOf<String>() to mutableListOf()
        }
        results[selectOp::class.simpleName]!!.first +=
            "$funName\t& ${initTimes.average()} ms\t& ${evaluationTimes.average()} ms\t& " +
            "${selectionTimes.average()} ms\t& ${alterationTimes.average()} ms\t& " +
            "${evolutionTimes.average()} s\\\\\\hline"
        results[selectOp::class.simpleName]!!.second +=
            "$funName\t& ${totalGenerations.average()}\t& \\((${
                fittests.map { it[0] }.average()
            },\\, ${fittests.map { it[1] }.average()})\\)\t& \\(${errors.average()}\\)\\\\\\hline"
    }
}

fun main() {
    Runner.run("Ackley", ::ackley, 0.0, RandomSelector(), -5.0..5.0)
    Runner.run("Beale", ::beale, 0.0, RandomSelector(), -4.5..4.5)
    Runner.run("Booth", ::booth, 0.0, RandomSelector(), -10.0..10.0)
    Runner.run("Bukin N.6", ::bukinN6, 0.0, RandomSelector(), -15.0..-5.0, -3.0..3.0)
    Runner.run("Cross-in-tray", ::crossInTray, -2.06261, RandomSelector(), -10.0..10.0)
    Runner.run("Easom", ::easom, -1.0, RandomSelector(), -100.0..100.0)
    Runner.run("Egg holder", ::eggholder, -959.6407, RandomSelector(), -512.0..512.0)
    Runner.run("Goldstein-Price", ::goldsteinPrice, 3.0, RandomSelector(), -2.0..2.0)
    Runner.run("Himmelblau", ::himmelblau, 0.0, RandomSelector(), -10.0..10.0)
    Runner.run("Holder table", ::holderTable, -19.2085, RandomSelector(), -10.0..10.0)
    Runner.run("Levi", ::levi, 0.0, RandomSelector(), -10.0..10.0)
    Runner.run("Matyas", ::matyas, 0.0, RandomSelector(), -10.0..10.0)
    Runner.run("McCormick", ::mccormick, -1.9133, RandomSelector(), -1.5..4.0, -3.0..4.0)
    Runner.run("Rastrigin", ::rastrigin, 0.0, RandomSelector(), -5.12..5.12)
    Runner.run("Rosenbrock", ::rosenbrock, 0.0, RandomSelector(), -2.048..2.048, -2.048..2.048)
    Runner.run("Schaffer N.2", ::schafferN2, 0.0, RandomSelector(), -100.0..100.0)
    Runner.run("Schaffer N.4", ::schafferN4, 0.0, RandomSelector(), -100.0..100.0)
    Runner.run("Styblinski-Tang", ::styblinskiTang, -39.16599, RandomSelector(), -5.0..5.0)
    Runner.run("Sphere", ::sphere, 0.0, RandomSelector(), -10.0..10.0)
    Runner.run("Three-hump camel", ::threeHumpCamel, 0.0, RandomSelector(), -5.0..5.0)

    Runner.run("Ackley", ::ackley, 0.0, TournamentSelector(3), -5.0..5.0)
    Runner.run("Beale", ::beale, 0.0, TournamentSelector(3), -4.5..4.5)
    Runner.run("Booth", ::booth, 0.0, TournamentSelector(3), -10.0..10.0)
    Runner.run("Bukin N.6", ::bukinN6, 0.0, TournamentSelector(3), -15.0..-5.0, -3.0..3.0)
    Runner.run("Cross-in-tray", ::crossInTray, -2.06261, TournamentSelector(3), -10.0..10.0)
    Runner.run("Easom", ::easom, -1.0, TournamentSelector(3), -100.0..100.0)
    Runner.run("Egg holder", ::eggholder, -959.6407, TournamentSelector(3), -512.0..512.0)
    Runner.run("Goldstein-Price", ::goldsteinPrice, 3.0, TournamentSelector(3), -2.0..2.0)
    Runner.run("Himmelblau", ::himmelblau, 0.0, TournamentSelector(3), -10.0..10.0)
    Runner.run("Holder table", ::holderTable, -19.2085, TournamentSelector(3), -10.0..10.0)
    Runner.run("Levi", ::levi, 0.0, TournamentSelector(3), -10.0..10.0)
    Runner.run("Matyas", ::matyas, 0.0, TournamentSelector(3), -10.0..10.0)
    Runner.run("McCormick", ::mccormick, -1.9133, TournamentSelector(3), -1.5..4.0, -3.0..4.0)
    Runner.run("Rastrigin", ::rastrigin, 0.0, TournamentSelector(3), -5.12..5.12)
    Runner.run("Rosenbrock", ::rosenbrock, 0.0, TournamentSelector(3), -2.048..2.048, -2.048..2.048)
    Runner.run("Schaffer N.2", ::schafferN2, 0.0, TournamentSelector(3), -100.0..100.0)
    Runner.run("Schaffer N.4", ::schafferN4, 0.0, TournamentSelector(3), -100.0..100.0)
    Runner.run("Styblinski-Tang", ::styblinskiTang, -39.16599, TournamentSelector(3), -5.0..5.0)
    Runner.run("Sphere", ::sphere, 0.0, TournamentSelector(3), -10.0..10.0)
    Runner.run("Three-hump camel", ::threeHumpCamel, 0.0, TournamentSelector(3), -5.0..5.0)

    Runner.run("Ackley", ::ackley, 0.0, RouletteWheelSelector(), -5.0..5.0)
    Runner.run("Beale", ::beale, 0.0, RouletteWheelSelector(), -4.5..4.5)
    Runner.run("Booth", ::booth, 0.0, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("Bukin N.6", ::bukinN6, 0.0, RouletteWheelSelector(), -15.0..-5.0, -3.0..3.0)
    Runner.run("Cross-in-tray", ::crossInTray, -2.06261, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("Easom", ::easom, -1.0, RouletteWheelSelector(), -100.0..100.0)
    Runner.run("Egg holder", ::eggholder, -959.6407, RouletteWheelSelector(), -512.0..512.0)
    Runner.run("Goldstein-Price", ::goldsteinPrice, 3.0, RouletteWheelSelector(), -2.0..2.0)
    Runner.run("Himmelblau", ::himmelblau, 0.0, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("Holder table", ::holderTable, -19.2085, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("Levi", ::levi, 0.0, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("Matyas", ::matyas, 0.0, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("McCormick", ::mccormick, -1.9133, RouletteWheelSelector(), -1.5..4.0, -3.0..4.0)
    Runner.run("Rastrigin", ::rastrigin, 0.0, RouletteWheelSelector(), -5.12..5.12)
    Runner.run("Rosenbrock", ::rosenbrock, 0.0, RouletteWheelSelector(), -2.048..2.048, -2.048..2.048)
    Runner.run("Schaffer N.2", ::schafferN2, 0.0, RouletteWheelSelector(), -100.0..100.0)
    Runner.run("Schaffer N.4", ::schafferN4, 0.0, RouletteWheelSelector(), -100.0..100.0)
    Runner.run("Styblinski-Tang", ::styblinskiTang, -39.16599, RouletteWheelSelector(), -5.0..5.0)
    Runner.run("Sphere", ::sphere, 0.0, RouletteWheelSelector(), -10.0..10.0)
    Runner.run("Three-hump camel", ::threeHumpCamel, 0.0, RouletteWheelSelector(), -5.0..5.0)

    Runner.results.forEach { (t, u) ->
        println("========= $t =========")
        println(u.first.joinToString("\n"))
        println()
        println(u.second.joinToString("\n"))
    }
}
