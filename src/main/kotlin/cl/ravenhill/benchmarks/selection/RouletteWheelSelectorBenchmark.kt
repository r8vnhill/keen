/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.benchmarks.selection

import cl.ravenhill.benchmarks.benchmarkFileExists
import cl.ravenhill.benchmarks.createWordGuessingEngine
import cl.ravenhill.benchmarks.initializeBenchmarkLogger
import cl.ravenhill.benchmarks.saveEvolutionToFile
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.util.nextString
import cl.ravenhill.kuro.Logger
import kotlin.random.Random
import kotlin.time.ExperimentalTime

/**
 * Constant variable that represents the logging tag used for logging purposes in the
 * RandomSelectorBenchmark class.
 */
private val logger = Logger.instance("RouletteWheelSelectorBenchmark")

@ExperimentalTime
fun main() {
    initializeBenchmarkLogger(logger)
    val category = "selection/random"
    var warmup = true
    for (i in listOf(1, 1, 20, 40, 60, 80, 100)) {
        repeat(3) { iteration ->
            if (benchmarkFileExists(category, i, iteration)) {
                logger.info { "Skipping $category/$i/$iteration" }
            } else {
                if (warmup) {
                    logger.info { "Warming up c:" }
                }
                val target = Random.Default.nextString(i)
                val engine = createWordGuessingEngine(target, RouletteWheelSelector())
                engine.evolve()
                if (warmup) {
                    warmup = false
                } else {
                    saveEvolutionToFile(engine, category, i, iteration)
                    logger.info { "Saved $category/$i/$iteration" }
                }
            }
        }
    }
}
