/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.genetic

import cl.ravenhill.keen.arbs.genetic.chromosomes.intChromosomeFactory
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary factory for creating instances of [Genotype] with [IntGene].
 *
 * This function creates [Genotype.Factory] instances specifically designed for genotypes composed of
 * integer genes ([IntGene]). It allows for the customization of the underlying chromosomes within the
 * genotype, making it versatile for various genetic algorithm scenarios.
 *
 * ## Functionality:
 * - The [chromosomeFactories] parameter determines the factories used to create the individual
 *   chromosomes within the genotype. Each factory in the list corresponds to a chromosome.
 * - The size and composition of the genotype are influenced by the number and configuration of
 *   chromosome factories provided.
 *
 * ## Example Usage:
 * ```
 * // Creating a genotype factory with a custom configuration for its chromosomes
 * val genotypeFactoryArb = Arb.intGenotypeFactory(
 *     list(Arb.intChromosomeFactory(), 1..5) // Configuring 1 to 5 chromosomes
 * )
 * val genotypeFactory = genotypeFactoryArb.bind()
 * // Creating a genotype using the factory
 * val genotype = genotypeFactory.make()
 * // The resulting genotype will have between 1 to 5 chromosomes, each created by the respective factory
 * ```
 *
 * This function is particularly useful for testing genetic algorithms that require genotypes
 * with integer genes and specific chromosome configurations.
 *
 * @param chromosomeFactories An [Arb] that generates a list of [IntChromosome.Factory] instances.
 *   Each factory in the list is responsible for creating a chromosome within the genotype. The default range for the
 *   number of chromosome factories is set from 0 to 10.
 * @return An [Arb] that generates instances of [Genotype.Factory] for producing genotypes composed
 *   of integer genes ([IntGene]), with chromosome configurations determined by the provided factories.
 */
fun Arb.Companion.intGenotypeFactory(
    chromosomeFactories: Arb<List<IntChromosome.Factory>> = list(intChromosomeFactory(), 0..10),
) = arbitrary {
    Genotype.Factory<Int, IntGene>().apply {
        chromosomes += chromosomeFactories.bind()
    }
}
