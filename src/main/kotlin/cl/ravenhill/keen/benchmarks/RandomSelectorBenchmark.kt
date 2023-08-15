/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.benchmarks

import cl.ravenhill.keen.builders.chars
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.RandomSelector
import cl.ravenhill.keen.util.listeners.serializers.JsonEvolutionSerializer
import cl.ravenhill.keen.util.nextString
import cl.ravenhill.kuro.Level
import cl.ravenhill.kuro.Logger
import cl.ravenhill.kuro.StdoutChannel
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.random.Random
import kotlin.time.ExperimentalTime

/***************************************************************************************************
 * A program that evolves a string of characters to match a target string using a genetic algorithm.
 * The fitness function is the number of characters in the genotype that match the target string.
 * The genetic algorithm uses a population size of 500, roulette wheel selection, mutation with a
 * probability of 0.03, and single point crossover with a probability of 0.06.
 * The algorithm stops when a genotype with the same length as the target string is found.
 **************************************************************************************************/

/**
 * Computes the fitness of a genotype by counting the number of characters that match the target
 * string.
 *
 * @return The fitness of the genotype.
 */
private fun kronecker(target: String): (Genotype<Char, CharGene>) -> Double = { g ->
    g.flatten()
        .filterIndexed { i, c -> c == target[i] }
        .size.toDouble()
}

/**
 * Runs the genetic algorithm to evolve a string of characters that matches the target string.
 */
@ExperimentalTime
fun main() {
    Logger.instance("RandomSelectorBenchmark").compositeChannel.add(StdoutChannel())
    Logger.instance("RandomSelectorBenchmark").level = Level.Info()
    Logger.instance("RandomSelectorBenchmark").info { "Starting benchmark" }
    runBlocking {
        for (i in listOf(1, 20, 40, 60, 80, 100)) {
            repeat(3) {
                val file = File("benchmarks/selection/random/$i/$it.json")
                if (file.exists()) {
                    Logger.instance("RandomSelectorBenchmark").info { "Skipping $i/$it" }
                } else {
                    val serializer = JsonEvolutionSerializer<Char, CharGene>()
                    val target = Random.Default.nextString(i)
                    val engine = engine(
                        kronecker(target),
                        genotype {
                            chromosome {
                                chars {
                                    size = target.length
                                }
                            }
                        }
                    ) {
                        populationSize = 500
                        selector = RandomSelector()
                        alterers = listOf(Mutator(0.06), SinglePointCrossover(0.2))
                        limits =
                            listOf(
                                TargetFitness(target.length.toDouble()),
                                GenerationCount(1000)
                            )
                        listeners = listOf(serializer)
                    }
                    engine.evolve()
                    serializer.saveToFile(file)
                    Logger.instance("RandomSelectorBenchmark").info { "Saved $i/$it" }
                }
            }
        }
    }
}
