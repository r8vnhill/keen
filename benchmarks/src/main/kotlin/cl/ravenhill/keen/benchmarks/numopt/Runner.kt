/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.numopt

import cl.ravenhill.keen.benchmarks.latex.Left
import cl.ravenhill.keen.benchmarks.latex.cell
import cl.ravenhill.keen.benchmarks.numopt.functions.OptimizationProblem
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.doubles
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.SteadyGenerations
import cl.ravenhill.keen.listeners.EvolutionSummary
import cl.ravenhill.keen.operators.crossover.AverageCrossover
import cl.ravenhill.keen.operators.mutation.RandomMutator
import cl.ravenhill.keen.operators.selection.RandomSelector
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import cl.ravenhill.keen.ranking.FitnessMinRanker
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.Duration

private const val BENCHMARK_ITERATIONS = 3
private const val WARMUP_ITERATIONS = 1
private const val POPULATION_SIZE = 500

/**
 * Represents the result of a benchmarking operation for an evolutionary algorithm.
 *
 * @property opName Name of the optimization operation.
 * @property funName Name of the function being optimized.
 * @property selectionTime Time taken for the selection process in the algorithm.
 * @property evolutionTime Time taken for the evolution process in the algorithm.
 * @property totalGenerations Total number of generations processed during the optimization.
 * @property fittest A pair representing the fittest solution found by the algorithm.
 * @property error The error value associated with the fittest solution.
 */
private data class BenchmarkResult(
    val opName: String,
    val funName: String,
    val selectionTime: Double,
    val evolutionTime: Double,
    val totalGenerations: Double,
    val fittest: Pair<Double, Double>,
    val error: Double,
) {
    operator fun plus(other: BenchmarkResult) = listOf(this, other)
}

fun createEngine(
    function: (Genotype<Double, DoubleGene>) -> Double,
    selectOp: Selector<Double, DoubleGene>,
    summary: EvolutionSummary<Double, DoubleGene>,
    ranges: List<ClosedRange<Double>>,
) = evolutionEngine(function, genotype {
    chromosomeOf {
        doubles {
            this.ranges += ranges
            this.size = 2
        }
    }
}) {
    ranker = FitnessMinRanker()
    populationSize = POPULATION_SIZE
    parentSelector = selectOp
    survivorSelector = selectOp
    alterers += listOf(RandomMutator(individualRate = 0.1), AverageCrossover(chromosomeRate = 0.3))
    listeners += summary
    limits += listOf(
        SteadyGenerations(generations = 50), MaxGenerations(generations = 500)
    )
}

private fun run(
    problem: OptimizationProblem,
    selectOp: Selector<Double, DoubleGene>,
): BenchmarkResult {
    val summaryListener = EvolutionSummary<Double, DoubleGene>(Duration::inWholeNanoseconds)
    var warmup = true
    val selectionTimes = mutableListOf<Double>()
    val evolutionTimes = mutableListOf<Double>()
    val totalGenerations = mutableListOf<Double>()
    val fittests = mutableListOf<List<Double>>()
    val errors = mutableListOf<Double>()
    repeat(BENCHMARK_ITERATIONS + WARMUP_ITERATIONS) {
        print(".")
        val engine = createEngine(problem::invoke, selectOp, summaryListener, problem.ranges)
        engine.evolve()
        if (warmup) {
            warmup = false
        } else {
            with(summaryListener) {
                fittests += fittest.toIndividual().flatMap()
                errors += abs(fittest.fitness - problem.target)
                with(evolution) {
                    selectionTimes += generations.map { it.parentSelection.duration / 1_000_000.0 }
                    evolutionTimes += duration / 1_000_000.0
                    totalGenerations += generations.size.toDouble() / 3
                }
            }
        }
    }
    return BenchmarkResult(
        selectOp::class.simpleName!!,
        problem.name,
        selectionTimes.average(),
        evolutionTimes.average(),
        totalGenerations.average(),
        fittests.map { it[0] }.average() to fittests.map { it[1] }.average(),
        errors.average(),
    )
}

fun standardDeviation(dataList: List<Double>): Double {
    if (dataList.isEmpty()) return 0.0

    val mean = dataList.average()
    val variance = dataList.sumOf { (it - mean).pow(2) } / dataList.size
    return sqrt(variance)
}

fun row(vararg values: Any) = values.joinToString(" &\t") { cell(it, 1, Left, Left, Left) } + "\\\\"

fun main() {
    val benchmarks = OptimizationProblem::class.sealedSubclasses.flatMap { kClass ->
        listOf(
            RandomSelector<Double, DoubleGene>(), TournamentSelector(), RouletteWheelSelector()
        ).map { selector ->
            run(kClass.objectInstance!!, selector)
        }
    }
    println()
    benchmarks.groupBy { it.opName }
        .forEach { (name, results) ->
            println("=================== $name ===================")
            results.forEach {
                println(row(it.funName, it.selectionTime, it.evolutionTime))
            }
            println("\\hline ")
            println(
                row(
                    "Average",
                    results.map { it.selectionTime }.average(),
                    results.map { it.evolutionTime }.average()
                )
            )
            println("\\hline")
            println(
                row(
                    "S. Deviation",
                    standardDeviation(results.map { it.selectionTime }),
                    standardDeviation(results.map { it.evolutionTime })
                )
            )

            println("-----------------------------------------------")
            results.forEach {
                println(row(it.funName, it.totalGenerations, it.fittest.first, it.fittest.second, it.error))
            }
            println("\\hline ")
            println("\\hline")
            println(
                row(
                    "Average",
                    results.map { it.totalGenerations }.average(),
                    "---",
                    "---",
                    results.map { it.error }.average(),
                )
            )
            println("\\hline")
            println(
                row(
                    "S. Deviation",
                    standardDeviation(results.map { it.totalGenerations }),
                    "---",
                    "---",
                    standardDeviation(results.map { it.error }),
                )
            )
            println("===============================================")
        }
}
