/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.genetic

import cl.ravenhill.keen.arb.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

/**
 * Generates an arbitrary [Genotype] for property-based testing.
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
    Genotype(list(arbChromosome(size = chromosomeSize, isValid = isValid), numChromosomes..numChromosomes).bind())
}

/**
 * Generates an arbitrary [Genotype] for property-based testing in genetic algorithms.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param chromosome An [Arb] of [Chromosome] for generating individual chromosomes within the genotype.
 * @param size An [Arb]<[Int]> specifying the potential number of chromosomes in the genotype.
 *   Defaults to a range of 0 to 5, allowing genotypes with up to 5 chromosomes.
 * @return An [Arb] that generates instances of [Genotype] with a specified number of chromosomes.
 */
@Deprecated("Use arbGenotype instead", ReplaceWith("arbGenotype(chromosome, size)"))
fun <T, G> Arb.Companion.genotype(
    chromosome: Arb<Chromosome<T, G>>,
    size: Arb<Int> = int(0..5),
) where G : Gene<T, G> = arbGenotype(chromosome, size)

/**
 * Creates an arbitrary generator for `Genotype<T, G>` instances, intended for property-based testing in the context of
 * evolutionary algorithms.
 *
 * @param T The type parameter representing the value type within the gene.
 * @param G The gene type, constrained to be a subclass of `Gene<T, G>`.
 * @param chromosome
 *  An `Arb<Chromosome<T, G>>` instance for generating the chromosomes within the genotype. It defines the type and
 *  characteristics of each chromosome.
 * @param size
 *  An optional `Arb<Int>` instance that specifies the number of chromosomes in the genotype, with a default range of 0
 *  to 5. This parameter allows for the customization of genotype size in the generated instances.
 * @return An `Arb<Genotype<T, G>>` that produces `Genotype` instances with the specified number and configuration of
 *  chromosomes.
 */
fun <T, G> arbGenotype(
    chromosome: Arb<Chromosome<T, G>>,
    size: Arb<Int> = Arb.int(0..5),
) where G : Gene<T, G> = arbitrary {
    val numChromosomes = size.bind()
    Genotype(Arb.list(chromosome, numChromosomes..numChromosomes).bind())
}

fun <T, G> arbGenotypeFactory(
    chromosomeFactory: Arb<List<Chromosome.Factory<T, G>>>,
): Arb<Genotype.Factory<T, G>> where G : Gene<T, G> = arbitrary {
    Genotype.Factory<T, G>().apply {
        chromosomeFactory.bind().forEach { chromosomes += it }
    }
}

fun arbValidChromosome(): Arb<Chromosome<Int, DummyGene>> = arbChromosome(isValid = Arb.constant(true))

fun arbValidGenotype(): Arb<Genotype<Int, DummyGene>> = arbGenotype(arbValidChromosome())
fun arbInvalidGenotype(): Arb<Genotype<Int, DummyGene>> =
    arbGenotype(arbChromosome())
        .filter { genotype -> genotype.chromosomes.isNotEmpty() && genotype.chromosomes.any { !it.verify() } }