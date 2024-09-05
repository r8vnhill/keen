/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.genetics.chromosomes.ChromosomeFactory
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.genetics.genotype.Genotype
import cl.ravenhill.keen.genetics.genotype.GenotypeFactory

class GenotypeScope<T, G : Gene<T, G>> {
    val chromosomes = mutableListOf<ChromosomeFactory<T, G>>()
}

/**
 * Creates a new [Genotype] with the given [init] block.
 *
 * Use this function to create a new [Genotype] instance with the specified chromosomes. The [init] block takes a
 * [GenotypeScope] instance, which can be used to add chromosomes to the genotype. Chromosomes can be specified using
 * the [chromosomeOf] function, which takes a lambda that returns a [ChromosomeFactory] instance.
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
 * @return A [GenotypeFactory] instance that contains the [ChromosomeFactory]s created by the
 *  [init] block.
 */
fun <T, G> genotypeOf(init: GenotypeScope<T, G>.() -> Unit) where G : Gene<T, G> =
    GenotypeFactory<T, G>().apply {
        chromosomes.addAll(GenotypeScope<T, G>().apply(init).chromosomes)
    }
