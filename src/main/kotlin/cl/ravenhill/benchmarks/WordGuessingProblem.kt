/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.benchmarks

import cl.ravenhill.keen.builders.chars
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.util.listeners.serializers.JsonEvolutionSerializer
import cl.ravenhill.kuro.Level
import cl.ravenhill.kuro.Logger
import cl.ravenhill.kuro.StdoutChannel
import java.io.File
import kotlin.time.ExperimentalTime

/**
 * Initializes the logger for benchmarking.
 */
internal fun initializeBenchmarkLogger(logger: Logger) {
    logger.compositeChannel.add(StdoutChannel())
    logger.level = Level.Info()
    logger.info { "Starting benchmark" }
}

/**
 * Evaluates how closely a genotype matches the target string by counting matching characters.
 *
 * @param target The target string to compare against.
 * @return A fitness function that computes the count of matching characters.
 */
internal fun matchingCharacterCount(target: String): (Genotype<Char, CharGene>) -> Double =
    { genotype ->
        genotype.flatten()
            .filterIndexed { index, char -> char == target[index] }
            .count()
            .toDouble()
    }

/**
 * Checks if a benchmark file exists based on the provided parameters.
 *
 * This function constructs a path to a potential benchmark file using the given category, word
 * length, and iteration.
 * It then checks if a file with the constructed path exists.
 *
 * @param category The category or grouping of the benchmarks.
 * @param wordLength The length of the words considered in the benchmarks.
 * @param iteration The specific iteration or run number of the benchmark.
 * @return A boolean indicating if the benchmark file exists. Returns true if the file exists,
 *         and false otherwise.
 */
internal fun benchmarkFileExists(category: String, wordLength: Int, iteration: Int): Boolean =
    File("benchmarks/$category/$wordLength/$iteration.json").exists()

/**
 * Creates an evolutionary engine configured specifically for character-based genomes.
 *
 * The function sets up an engine to evolve characters with a specific target string. It uses a
 * given target to determine the chromosome size. The evolution process utilizes both mutation
 * and crossover as altering mechanisms, and has set limitations in terms of target fitness and
 * generation count.
 *
 * @param target The target string to be evolved toward. Used to determine chromosome size.
 * @return An [Engine] configured for evolving a character-based genome towards the given target.
 */
@OptIn(ExperimentalTime::class)
internal fun createWordGuessingEngine(
    target: String,
    selector: Selector<Char, CharGene>,
): Engine<Char, CharGene> {
    val serializer = JsonEvolutionSerializer<Char, CharGene>()
    return engine(
        matchingCharacterCount(target),
        genotype {
            chromosome {
                chars {
                    size = target.length
                }
            }
        }
    ) {
        populationSize = 500
        this.selector = selector
        alterers = listOf(Mutator(0.06), SinglePointCrossover(0.2))
        limits = listOf(TargetFitness(target.length.toDouble()), GenerationCount(1000))
        listeners = listOf(serializer)
    }
}

@ExperimentalTime
internal fun saveEvolutionToFile(
    engine: Engine<Char, CharGene>,
    category: String,
    i: Int,
    iteration: Int,
) {
    val serializer = engine.listeners[0] as JsonEvolutionSerializer<Char, CharGene>
    val file = File("benchmarks/$category/$i/$iteration.json")
    serializer.saveToFile(file)
}
