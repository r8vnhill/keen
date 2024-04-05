/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.genetic

import cl.ravenhill.keen.arb.genetic.chromosomes.chromosome
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary [Genotype] for property-based testing.
 *
 * This function creates instances of [Genotype] with a configurable number of chromosomes, making it versatile for
 * testing scenarios involving evolutionary algorithms. The size of the genotype, i.e., the number of chromosomes it
 * contains, is determined by the provided `size` parameter. Each chromosome within the genotype is generated using the
 * `chromosome()` function.
 *
 * ## Usage:
 * This arbitrary generator can be used in property-based testing frameworks like Kotest to create diverse
 * instances of [Genotype], facilitating comprehensive testing across a range of genetic configurations.
 *
 * ### Example:
 * Generating a genotype with a random number of chromosomes (up to 5):
 * ```kotlin
 * val genotypeArb = Arb.genotype(Arb.int(0..5))
 * val genotype = genotypeArb.bind() // Resulting genotype will have between 0 to 5 chromosomes
 * ```
 *
 * This function is particularly useful in scenarios where genotypes of varying sizes and chromosome
 * configurations are needed to robustly test evolutionary algorithms and related functionalities.
 *
 * @param size An [Arb]<[Int]> that specifies the possible number of chromosomes in the genotype.
 *   Defaults to a range of 0 to 5, allowing genotypes with up to 5 chromosomes.
 * @return An [Arb] that generates instances of [Genotype] with a specified number of chromosomes.
 */
@Deprecated("Use the genotype function that takes a chromosome arb as parameter")
fun Arb.Companion.genotype(
    size: Arb<Int> = int(0..5),
    isValid: Arb<Boolean> = boolean(),
    chromosomeSize: Arb<Int> = int(0..5),
) = arbitrary {
    val numChromosomes = size.bind()
    Genotype(list(chromosome(size = chromosomeSize, isValid = isValid), numChromosomes..numChromosomes).bind())
}

/**
 * Generates an arbitrary [Genotype] for property-based testing in genetic algorithms.
 *
 * This function produces instances of [Genotype] with a configurable number of chromosomes, enabling versatility in
 * testing scenarios involving gene-centric evolutionary algorithms. The number of chromosomes in the genotype, i.e.,
 * the size of the genotype, is determined by the provided `size` parameter. Each chromosome within the genotype is
 * generated using the [chromosome] generator.
 *
 * ## Usage:
 * This arbitrary generator is useful in property-based testing frameworks like Kotest for creating diverse instances of
 * [Genotype], thus facilitating comprehensive testing across various genetic configurations.
 *
 * ### Example:
 * Generating a genotype with a specific number of chromosomes:
 * ```kotlin
 * val chromosomeArb = Arb.chromosome<MyDataType, MyGene>(/* ... */)
 * val genotypeArb = Arb.genotype(chromosomeArb, Arb.constant(3)) // Configuring exactly 3 chromosomes
 * val genotype = genotypeArb.bind() // Resulting genotype will have 3 chromosomes
 * ```
 * In this example, `genotypeArb` generates a genotype with exactly three chromosomes, each created
 * by the provided `chromosomeArb`.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param chromosome An [Arb] of [Chromosome] for generating individual chromosomes within the genotype.
 * @param size An [Arb]<[Int]> specifying the potential number of chromosomes in the genotype.
 *   Defaults to a range of 0 to 5, allowing genotypes with up to 5 chromosomes.
 * @return An [Arb] that generates instances of [Genotype] with a specified number of chromosomes.
 */
fun <T, G> Arb.Companion.genotype(
    chromosome: Arb<Chromosome<T, G>>,
    size: Arb<Int> = int(0..5),
) where G : Gene<T, G> = arbitrary {
    val numChromosomes = size.bind()
    Genotype(list(chromosome, numChromosomes..numChromosomes).bind())
}

fun <T, G> arbGenotypeFactory(
    chromosomeFactory: Arb<List<Chromosome.Factory<T, G>>>,
): Arb<Genotype.Factory<T, G>> where G : Gene<T, G> = arbitrary {
    Genotype.Factory<T, G>().apply {
        chromosomeFactory.bind().forEach { chromosomes += it }
    }
}
