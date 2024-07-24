/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Creates and configures an instance of [GeneticAlgorithm] for evolutionary algorithms.
 *
 * This function serves as a convenient builder for setting up an [GeneticAlgorithm]. It encapsulates the configuration
 * and creation of the engine in a concise and readable manner. The engine is customized using a provided fitness
 * function, a genotype factory, and an initialization block that allows for further configuration.
 *
 * ## Usage:
 * ```kotlin
 * val engine = evolutionEngine(
 *     fitnessFunction = { genotype -> /* Calculate fitness */ },
 *     genotype = MyGenotypeFactory
 * ) {
 *     populationSize = 100
 *     survivalRate = 0.5
 *     // Additional configurations...
 * }
 * ```
 * In this example, the `evolutionEngine` function is used to create an `EvolutionEngine`. The engine is configured
 * with a custom fitness function, a genotype factory, and additional settings defined in the lambda block.
 *
 * Note that the generic types of the engine are inferred from the provided parameters, so it is not necessary to
 * specify them explicitly.
 *
 * @param fitnessFunction A function that calculates the fitness of a genotype. This function is central to the
 *   evolutionary process, determining how well each individual performs in the given context.
 * @param genotype A factory for creating genotypes, which are the fundamental units of genetic information in the
 *   evolutionary algorithm.
 * @param init A lambda block for additional configuration. This block is applied to an instance of
 *   [GeneticAlgorithm.Factory], allowing for detailed customization of the evolutionary process.
 * @return An instance of [GeneticAlgorithm] configured as per the provided parameters and initialization block.
 */
fun <T, G> evolutionEngine(
    fitnessFunction: (Genotype<T, G>) -> Double,
    genotype: Genotype.Factory<T, G>,
    init: GeneticAlgorithm.Factory<T, G>.() -> Unit,
) where G : Gene<T, G> = GeneticAlgorithm.Factory(fitnessFunction, genotype).apply(init).make()
