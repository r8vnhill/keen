/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

@file:Suppress("UnusedReceiverParameter")

package cl.ravenhill.keen.builders

import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.ProgramChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.Gene

/***************************************************************************************************
 * This is a Kotlin file that contains a DSL for configuring genetic algorithms.
 * It allows the user to define and customize the genotype of a population.
 * Specifically, it provides a chromosome function that adds a chromosome factory to the builder,
 * which can be used to specify the characteristics of a chromosome that should be used to build a
 * genotype.
 * The chromosome can be of various types including boolean, character, integer, double, and
 * program chromosomes.
 * The chromosome function takes a lazyFactory function that returns a Chromosome.Factory instance
 * for the chromosome.
 * The file contains several extension functions that allow the user to configure each type of
 * chromosome using a lambda block. For instance, the booleans function creates a new
 * BoolChromosome.Factory instance with the given builder block, and the ints function creates a new
 * IntChromosome.Factory.
 **************************************************************************************************/

/**
 * A DSL scope for configuring a chromosome factory for a genotype builder.
 *
 * Use this scope to specify the characteristics of a chromosome that should be used to build a
 * genotype. A chromosome is a sequence of genetic data that will be inherited as a unit to the
 * offspring during genetic operations.
 *
 * @param DNA the type of the data carried by each gene in the chromosome.
 *
 * @see Chromosome
 * @see GenotypeScope
 */
class ChromosomeScope<DNA>


/**
 * Adds a chromosome factory to the builder.
 *
 * __Usage:__
 * ```
 * genotype {
 *   chromosome {
 *     booleans { }
 *   }
 * }
 * ```
 *
 * @param lazyFactory A function that returns a [Chromosome.Factory] instance for the
 *      chromosome.
 * @return `true` if the element was added, `false` if not.
 *      This will always return `true`.
 *
 * @param DNA The type of data that the chromosomes are made of.
 * @param G The type of gene that the chromosomes contain.
 */
fun <DNA, G : Gene<DNA, G>> GenotypeScope<DNA, G>.chromosome(
    lazyFactory: ChromosomeScope<DNA>.() -> Chromosome.Factory<DNA, G>
) = chromosomes.add(ChromosomeScope<DNA>().lazyFactory())


/**
 * Creates a new [BoolChromosome.Factory] with the given [builder] block.
 *
 * __Usage:__
 * ```
 * genotype {
 *   chromosome {
 *     booleans {
 *       size = 20
 *       truesProbability = 0.15
 *     }
 *   }
 * }
 * ```
 *
 * @param builder A lambda block that allows configuring the [BoolChromosome.Factory] by
 *  specifying its properties.
 *
 * @return A [BoolChromosome.Factory] instance with the specified properties.
 *
 * @see BoolChromosome
 * @see Chromosome
 */
fun ChromosomeScope<Boolean>.booleans(builder: BoolChromosome.Factory.() -> Unit) =
    BoolChromosome.Factory().apply(builder)

/**
 * Creates a new [CharChromosome.Factory] with the given [builder] block.
 *
 * __Usage:__
 * ```
 * genotype {
 *   chromosome {
 *     chars {
 *       size = 20
 *       range = 'a'..'z'
 *     }
 *   }
 * }
 * ```
 *
 * @param builder A lambda block that allows configuring the [CharChromosome.Factory] by
 *  specifying its properties.
 *
 * @return A `CharChromosome.Factory` instance with the specified properties.
 */
fun ChromosomeScope<Char>.chars(builder: CharChromosome.Factory.() -> Unit) =
    CharChromosome.Factory().apply(builder)

/**
 * Creates a new [IntChromosome.Factory].
 *
 * __Usage:__
 * ```
 * genotype {
 *   chromosome {
 *     ints {
 *       size = 20
 *       range = 0..100
 *       filter = { it % 2 == 0 }
 *     }
 *   }
 * }
 * ```
 *
 * @param builder A lambda block that allows configuring the properties of the
 *  [IntChromosome].
 *
 * @return An [IntChromosome.Factory] instance that can be used to create new
 *  [IntChromosome] instances.
 */
fun ChromosomeScope<Int>.ints(builder: IntChromosome.Factory.() -> Unit) =
    IntChromosome.Factory().apply(builder)

/**
 * Creates a new [DoubleChromosome.Factory] with the given [builder] block.
 *
 * __Usage:__
 * ```
 * chromosome {
 *     doubles {
 *         size = 20
 *         range = 0.0..100.0
 *     }
 * }
 * ```
 *
 * @param builder A lambda block that allows configuring the properties of the
 *  [DoubleChromosome].
 *
 * @return A [DoubleChromosome.Factory] instance that can be used to create new
 *  [DoubleChromosome] instances.
 */
fun ChromosomeScope<Double>.doubles(builder: DoubleChromosome.Factory.() -> Unit) =
    DoubleChromosome.Factory().apply(builder)

/**
 * Creates a new [ProgramChromosome.Factory] using the specified builder to configure it.
 *
 * __Usage:__
 * ```
 * program {
 *     function("*", 2) { it[0] * it[1] }
 *     function("+", 2) { it[0] + it[1] }
 *     terminal { EphemeralConstant { Core.random.nextInt(-1, 2).toDouble() } }
 *     terminal { Variable("x", 0) }
 * }
 * ```
 *
 * @param builder The lambda block that configures the properties of the [ProgramChromosome].
 *
 * @return A [ProgramChromosome.Factory] instance that can be used to create new
 *  [ProgramChromosome] instances.
 */
fun <T> program(builder: ProgramChromosome.Factory<T>.() -> Unit) =
    ProgramChromosome.Factory<T>().apply(builder)