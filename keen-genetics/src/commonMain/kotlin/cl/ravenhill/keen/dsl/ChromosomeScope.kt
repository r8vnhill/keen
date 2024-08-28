/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


// "Unused" receivers are used to allow the DSL to define functions that are only available inside a scope.
@file:Suppress("UnusedReceiverParameter")

package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.genetics.chromosomes.BooleanChromosomeFactory
import cl.ravenhill.keen.genetics.chromosomes.ChromosomeFactory
import cl.ravenhill.keen.genetics.genes.Gene

class ChromosomeScope<T>

fun <T, G> GenotypeScope<T, G>.chromosomeOf(
    lazyFactory: ChromosomeScope<T>.() -> ChromosomeFactory<T, G>
) where G : Gene<T, G> = chromosomes.add(ChromosomeScope<T>().lazyFactory())

fun ChromosomeScope<Boolean>.booleans(builder: BooleanChromosomeFactory.() -> Unit) =
    BooleanChromosomeFactory().apply(builder)

///**
// * Configures and creates a [CharChromosome.Factory] within a [ChromosomeScope]<[Char]>.
// *
// * This function provides a convenient way to create a `CharChromosome.Factory` instance with custom configurations
// * specified within a `ChromosomeScope<Char>`. It allows for the succinct setup of a factory for [CharChromosome]
// * instances using a builder pattern. The `builder` lambda provides the configuration mechanism, where you can set
// * various properties like `ranges`, `filters`, and `size` for the factory.
// *
// * ## Usage:
// * This function is typically used within a genotype building block, where a chromosome of character values is needed.
// * The `builder` lambda enables detailed configuration of the chromosome factory, including aspects like gene ranges,
// * filters, and other properties relevant to a chromosome of character values.
// *
// * ### Example:
// * ```kotlin
// * val genotype = genotypeOf {
// *     chromosomeOf {
// *         chars {
// *             size = 10
// *             ranges += 'a'..'z'
// *             filters += { it.isLowerCase() }
// *         }
// *     }
// * }
// * ```
// * In this example, a factory is configured within a `ChromosomeScope<Char>` to create `CharChromosome` instances of
// * size 10, with each gene having a range of 'a' to 'z' and a filter that ensures each character is lowercase. The
// * factory is then used to create a chromosome based on these specifications.
// *
// * @param builder A lambda function with `CharChromosome.Factory` as its receiver, allowing for custom configuration
// *    of the factory.
// * @return A configured `CharChromosome.Factory` instance.
// */
//fun ChromosomeScope<Char>.chars(builder: CharChromosome.Factory.() -> Unit) =
//    CharChromosome.Factory().apply(builder)
//
///**
// * Defines a chromosome of double values within a genotype building process.
// *
// * This function is part of the `ChromosomeScope<Double>` DSL (Domain-Specific Language), facilitating
// * the construction of a chromosome composed of double values. It allows the configuration of a
// * `DoubleChromosome.Factory` through a DSL-style builder.
// *
// * ## Usage:
// * This function is used within a genotype building block where chromosomes of double values are needed.
// * The `builder` lambda enables detailed configuration of the chromosome factory, including aspects like
// * gene ranges, filters, and other properties relevant to a chromosome of double values.
// *
// * ### Example:
// * ```kotlin
// * genotypeOf {
// *     chromosomeOf {
// *         doubles {
// *             size = 5
// *             ranges += 0.0..10.0
// *             // Additional configurations for the double chromosome...
// *         }
// *     }
// * }
// * ```
// * In this example, a chromosome of double values is added to the genotype. The `doubles` function
// * is used within a `chromosomeOf` block, where the `DoubleChromosome.Factory` is configured to create
// * a chromosome with specific characteristics.
// *
// * @receiver [ChromosomeScope]<[Double]> The scope within which this function is applicable, specialized for double
// *   values.
// * @param builder A lambda function with receiver [DoubleChromosome.Factory] for configuring the factory.
// */
//fun ChromosomeScope<Double>.doubles(builder: DoubleChromosome.Factory.() -> Unit) =
//    DoubleChromosome.Factory().apply(builder)
//
///**
// * Defines a chromosome of integer values within a genotype building process.
// *
// * This function is part of the `ChromosomeScope<Int>` DSL (Domain-Specific Language), facilitating the construction
// * of a chromosome composed of integer values. It allows the configuration of an `IntChromosome.Factory` through a
// * DSL-style builder.
// *
// * ## Usage:
// * This function is used within a genotype building block where chromosomes of integer values are needed.
// * The `builder` lambda enables detailed configuration of the chromosome factory, including aspects like gene ranges,
// * filters, and other properties relevant to a chromosome of integer values.
// *
// * ### Example:
// * ```kotlin
// * genotypeOf {
// *     chromosomeOf {
// *         integers {
// *             size = 5
// *             ranges += 0..10
// *         }
// *     }
// * }
// * ```
// * In this example, a chromosome of integer values is added to the genotype. The `ints` function is used within a
// * `chromosomeOf` block, where the `IntChromosome.Factory` is configured to create a chromosome with specific
// * characteristics.
// *
// * @receiver [ChromosomeScope]<[Int]> The scope within which this function is applicable, specialized for integer
// *  values.
// * @param builder A lambda function with receiver [IntChromosome.Factory] for configuring the factory.
// */
//fun ChromosomeScope<Int>.integers(builder: IntChromosome.Factory.() -> Unit) = IntChromosome.Factory().apply(builder)
