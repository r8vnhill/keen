/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Creates and initializes an evolution engine using the specified fitness function and genotype factory.
 *
 * This function is deprecated and should be replaced with `geneticAlgorithm`. It simplifies the creation and
 * initialization of an evolution engine by accepting a fitness function, a genotype factory, and an initialization
 * block. The function configures and creates an instance of `GeneticAlgorithm` through its factory.
 *
 * ## Usage:
 * This function was used to set up an evolution engine with custom configurations. It is recommended to use
 * `geneticAlgorithm` instead for better clarity and consistency.
 *
 * ### Example:
 * ```
 * val engine = evolutionEngine(
 *     fitnessFunction = { genotype -> /* Calculate fitness */ },
 *     genotype = MyGenotypeFactory
 * ) {
 *     populationSize = 100
 *     survivalRate = 0.5
 *     // Additional configurations...
 * }
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param fitnessFunction The function used to evaluate the fitness of the individual's representation.
 * @param genotype The factory used to create genotypes.
 * @param init The initialization block for configuring the genetic algorithm factory.
 * @return An initialized instance of `GeneticAlgorithm`.
 */
@Deprecated("Use geneticAlgorithm instead", ReplaceWith("geneticAlgorithm(fitnessFunction, genotype, init)"))
fun <T, G> evolutionEngine(
    fitnessFunction: (Representation<T, G>) -> Double,
    genotype: Genotype.Factory<T, G>,
    init: GeneticAlgorithm.Factory<T, G>.() -> Unit,
) where G : Gene<T, G> = geneticAlgorithm(fitnessFunction, genotype, init)

/**
 * Creates and initializes a genetic algorithm using the specified fitness function and genotype factory.
 *
 * The `geneticAlgorithm` function simplifies the creation and initialization of a genetic algorithm by providing
 * a convenient inline function that accepts a fitness function, a genotype factory, and an initialization block.
 * It uses the provided parameters to configure and create an instance of `GeneticAlgorithm` through its factory.
 *
 * ## Usage:
 * This function is useful for quickly setting up a genetic algorithm with custom configurations. The initialization
 * block allows for specifying additional configurations on the genetic algorithm factory before creating the final
 * instance.
 *
 * ### Example:
 * ```
 * val engine = geneticAlgorithm(
 *     fitnessFunction = { genotype -> /* Calculate fitness */ },
 *     genotype = MyGenotypeFactory
 * ) {
 *     populationSize = 100
 *     survivalRate = 0.5
 *     // Additional configurations...
 * }
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param fitnessFunction The function used to evaluate the fitness of the individual's representation.
 * @param genotype The factory used to create genotypes.
 * @param init The initialization block for configuring the genetic algorithm factory.
 * @return An initialized instance of `GeneticAlgorithm`.
 */
inline fun <T, G> geneticAlgorithm(
    noinline fitnessFunction: (Representation<T, G>) -> Double,
    genotype: Genotype.Factory<T, G>,
    init: GeneticAlgorithm.Factory<T, G>.() -> Unit,
) where G : Gene<T, G> = GeneticAlgorithm.Factory(fitnessFunction, genotype).apply(init).make()
