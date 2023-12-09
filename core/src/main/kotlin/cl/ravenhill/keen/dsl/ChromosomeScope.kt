/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


// "Unused" receivers are used to allow the DSL to define functions that are only available inside a scope.
@file:Suppress("UnusedReceiverParameter")

package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.genetic.chromosomes.BooleanChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A DSL scope for configuring a chromosome factory for a genotype builder.
 *
 * Use this scope to specify the characteristics of a chromosome that should be used to build a
 * genotype. A chromosome is a sequence of genetic data that will be inherited as a unit to the
 * offspring during genetic operations.
 *
 * @param T the type of the data carried by each gene in the chromosome.
 *
 * @see Chromosome
 * @see GenotypeScope
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class ChromosomeScope<T>

/**
 * Adds a chromosome to the genotype using a lazily evaluated factory.
 *
 * This function is part of the `GenotypeScope` DSL (Domain-Specific Language), which is used for building genotypes in
 * a declarative manner. It allows the addition of a chromosome to the genotype being constructed by providing a factory
 * that is lazily evaluated. This approach offers flexibility in defining the chromosomes' properties and behavior.
 *
 * ## Usage:
 * This function is typically used within a genotype building block, where multiple chromosomes might be
 * added to form a complete genotype. Each call to `chromosomeOf` adds a new chromosome to the genotype, with
 * its configuration specified by the provided `lazyFactory`.
 *
 * ### Example:
 * ```kotlin
 * val genotype = genotype {
 *     chromosomeOf {
 *         doubles {
 *           // Configuration of the double chromosome...
 *         }
 *     }
 *     chromosomeOf {
 *         doubles {
 *           // Configuration of the double chromosome...
 *         }
 *     }
 * }
 * ```
 * In this example, the `genotype` block contains two `chromosomeOf` calls, each adding a chromosome with a
 * specific factory. The `factory` method within `ChromosomeScope` is used to specify the chromosome's
 * creation logic.
 *
 * @param T The type of data encapsulated by the genes in the chromosome.
 * @param G The specific type of [Gene] associated with the chromosome.
 * @param lazyFactory A lambda function with receiver [ChromosomeScope]<[T]> that provides a
 *   [Chromosome.Factory]<[T], [G]>.
 * @return `true` as the element is always added to the genotype.
 */
fun <T, G> GenotypeScope<T, G>.chromosomeOf(
    lazyFactory: ChromosomeScope<T>.() -> Chromosome.Factory<T, G>,
) where G : Gene<T, G> = chromosomes.add(ChromosomeScope<T>().lazyFactory())

/**
 * Configures and creates a `BooleanChromosome.Factory` within a `ChromosomeScope<Boolean>`.
 *
 * This function provides a convenient way to create a `BooleanChromosome.Factory` instance with custom configurations
 * specified within a `ChromosomeScope<Boolean>`. It allows for the succinct setup of a factory for `BooleanChromosome`
 * instances using a builder pattern. The `builder` lambda provides the configuration mechanism, where you can set
 * various properties like `trueRate` and `size` for the factory.
 *
 * ## Parameters:
 * - `builder`: A lambda function with `BooleanChromosome.Factory` as its receiver, allowing for custom configuration
 *   of the factory.
 *
 * ## Usage:
 * This function is typically used within a genotype building block, where a chromosome of boolean values is needed.
 * The `builder` lambda enables detailed configuration of the chromosome factory, including aspects like gene ranges,
 * filters, and other properties relevant to a chromosome of boolean values.
 *
 * ### Example:
 * ```kotlin
 * genotype {
 *     chromosomeOf {
 *         booleans {
 *             size = 10
 *             trueRate = 0.7
 *         }
 *     }
 * }
 * ```
 * In this example, `myChromosomeFactory` is configured within a `ChromosomeScope<Boolean>` to create
 * `BooleanChromosome` instances of size 10, with a 70% probability of each gene being `true`. The `make` method is then
 * used to create a chromosome based on these specifications.
 *
 * @return A configured `BooleanChromosome.Factory` instance.
 */
fun ChromosomeScope<Boolean>.booleans(builder: BooleanChromosome.Factory.() -> Unit) =
    BooleanChromosome.Factory().apply(builder)

/**
 * Defines a chromosome of double values within a genotype building process.
 *
 * This function is part of the `ChromosomeScope<Double>` DSL (Domain-Specific Language), facilitating
 * the construction of a chromosome composed of double values. It allows the configuration of a
 * `DoubleChromosome.Factory` through a DSL-style builder.
 *
 * ## Usage:
 * This function is used within a genotype building block where chromosomes of double values are needed.
 * The `builder` lambda enables detailed configuration of the chromosome factory, including aspects like
 * gene ranges, filters, and other properties relevant to a chromosome of double values.
 *
 * ### Example:
 * ```kotlin
 * genotype {
 *     chromosomeOf {
 *         doubles {
 *             size = 5
 *             ranges += 0.0..10.0
 *             // Additional configurations for the double chromosome...
 *         }
 *     }
 * }
 * ```
 * In this example, a chromosome of double values is added to the genotype. The `doubles` function
 * is used within a `chromosomeOf` block, where the `DoubleChromosome.Factory` is configured to create
 * a chromosome with specific characteristics.
 *
 * @receiver [ChromosomeScope]<[Double]> The scope within which this function is applicable, specialized for double
 *   values.
 * @param builder A lambda function with receiver [DoubleChromosome.Factory] for configuring the factory.
 */
fun ChromosomeScope<Double>.doubles(builder: DoubleChromosome.Factory.() -> Unit) =
    DoubleChromosome.Factory().apply(builder)

/**
 * Defines a chromosome of integer values within a genotype building process.
 *
 * This function is part of the `ChromosomeScope<Int>` DSL (Domain-Specific Language), facilitating the construction
 * of a chromosome composed of integer values. It allows the configuration of an `IntChromosome.Factory` through a
 * DSL-style builder.
 *
 * ## Usage:
 * This function is used within a genotype building block where chromosomes of integer values are needed.
 * The `builder` lambda enables detailed configuration of the chromosome factory, including aspects like gene ranges,
 * filters, and other properties relevant to a chromosome of integer values.
 *
 * ### Example:
 * ```kotlin
 * genotype {
 *     chromosomeOf {
 *         integers {
 *             size = 5
 *             ranges += 0..10
 *         }
 *     }
 * }
 * ```
 * In this example, a chromosome of integer values is added to the genotype. The `ints` function is used within a
 * `chromosomeOf` block, where the `IntChromosome.Factory` is configured to create a chromosome with specific
 * characteristics.
 *
 * @receiver [ChromosomeScope]<[Int]> The scope within which this function is applicable, specialized for integer
 *  values.
 * @param builder A lambda function with receiver [IntChromosome.Factory] for configuring the factory.
 */
fun ChromosomeScope<Int>.integers(builder: IntChromosome.Factory.() -> Unit) = IntChromosome.Factory().apply(builder)
