/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.builders

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A scope for configuring a [Genotype] instance by adding chromosome factories.
 *
 * A genotype is a collection of chromosomes, each of which contains genetic data that is passed
 * on to offspring during genetic operations. Use this scope to specify the chromosomes to be used
 * when creating a genotype.
 *
 * @property chromosomes A list of chromosome factories that will be used to create the genotype.
 *
 * @see ChromosomeScope
 */
class GenotypeScope<DNA, G : Gene<DNA, G>> {
    val chromosomes = mutableListOf<Chromosome.Factory<DNA, G>>()
}

/**
 * Creates a new [Genotype] with the given [init] block.
 *
 * Use this function to create a new [Genotype] instance with the specified chromosomes. The
 * [init] block takes a [GenotypeScope] instance, which can be used to add chromosomes to the
 * genotype. Chromosomes can be specified using the [chromosome] function, which takes a lambda
 * that returns a [Chromosome.Factory] instance.
 *
 * __Example usage:__
 * ```
 * genotype {
 *     chromosome {
 *         booleans { }
 *     }
 * }
 * ```
 *
 * @param init A lambda block that allows configuring the genotype by specifying its chromosomes.
 *
 * @return A [Genotype.Factory] instance that contains the [Chromosome.Factory]s created by the
 *  [init] block.
 */
fun <DNA, G : Gene<DNA, G>> genotype(init: GenotypeScope<DNA, G>.() -> Unit) =
    Genotype.Factory<DNA, G>().apply {
        chromosomes.addAll(GenotypeScope<DNA, G>().apply(init).chromosomes)
    }