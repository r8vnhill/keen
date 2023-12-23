/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.next

/**
 * Creates an arbitrary generator for [Alterer]<[T], [G]> instances.
 *
 * This function, residing within the [Arb.Companion] object, generates arbitrary instances of `Alterer<T, G>`.
 * An `Alterer` in genetic algorithms is a component that performs genetic operations like mutation or
 * crossover on a population of individuals. The implementation provided here is a basic example that simply truncates
 * the population to a specified `outputSize`. This function is particularly useful for creating custom alterers in a
 * testing environment.
 *
 * ## Functionality:
 * - Generates `Alterer` instances that truncate the population of individuals.
 * - Operates by taking the first `outputSize` individuals from the current population.
 *
 * ## Usage:
 * Employ this arbitrary to generate custom `Alterer` instances in scenarios involving genetic algorithms, especially
 * for testing and experimentation purposes. It allows for observing the effects of different population truncation
 * strategies on the evolution process.
 *
 * ### Example:
 * ```kotlin
 * val altererGen = Arb.alterer<Int, SomeGeneClass>()
 * val alterer = altererGen.bind() // Generates an Alterer instance
 * // Use alterer in a genetic algorithm to manipulate the population size
 * ```
 * In this example, `altererGen` is an arbitrary that generates an instance of `Alterer`. The generated alterer can then
 * be used in a genetic algorithm to truncate the population to a certain size, based on specific requirements or
 * conditions of the algorithm.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @return An [Arb]<[Alterer]<[T], [G]>> for generating `Alterer` instances.
 */
fun <T, G> Arb.Companion.alterer(): Arb<Alterer<T, G>> where G : Gene<T, G> = arbitrary {
    object : Alterer<T, G> {
        override fun invoke(state: EvolutionState<T, G>, outputSize: Int) =
            state.copy(population = state.population.take(outputSize))
    }
}

/**
 * Creates an arbitrary generator for [Mutator]<[T], [G]> instances with configurable mutation rates.
 *
 * Part of the [Arb.Companion] object, this function generates arbitrary instances of `Mutator<T, G>`. A `Mutator` in
 * genetic algorithms is responsible for introducing variations in individuals' genetic makeup. This implementation
 * allows setting the mutation rates for individuals and chromosomes, providing flexibility in how mutation is applied
 * in the evolutionary process. The `individualRate` determines the likelihood of an individual being mutated, while the
 * `chromosomeRate` sets the probability of mutation occurring within a chromosome.
 *
 * ## Functionality:
 * - Generates `Mutator` instances with customizable mutation rates for individuals and chromosomes.
 * - `individualRate` and `chromosomeRate` are probabilities ranging from 0.0 to 1.0.
 *
 * ## Usage:
 * Utilize this arbitrary to create `Mutator` instances with specific mutation rates, useful in scenarios involving
 * genetic algorithms where fine-tuning of mutation behavior is necessary. It is particularly useful for testing
 * different mutation strategies and observing their impact on the evolution of populations.
 *
 * ### Example:
 * ```kotlin
 * val baseMutatorGen = Arb.baseMutator<Int, SomeGeneClass>()
 * val baseMutator = baseMutatorGen.bind() // Generates a Mutator instance with default mutation rates
 * // Use baseMutator in a genetic algorithm to apply mutations to individuals and chromosomes
 * ```
 * In this example, `baseMutatorGen` generates a `Mutator` instance. This mutator can be used in a genetic algorithm to
 * introduce variations in the genetic structure of individuals, with control over the mutation rates at both individual
 * and chromosome levels.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param individualRate An optional [Arb]<[Double]> for the individual mutation rate. Defaults to a probability value.
 * @param chromosomeRate An optional [Arb]<[Double]> for the chromosome mutation rate. Defaults to a probability value.
 * @return An [Arb]<[Mutator]<[T], [G]>> for generating `Mutator` instances with varying mutation rates.
 */
fun <T, G> Arb.Companion.baseMutator(
    individualRate: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
): Arb<Mutator<T, G>> where G : Gene<T, G> = arbitrary {
    object : Mutator<T, G> {
        override val individualRate = individualRate.next()
        override val chromosomeRate = chromosomeRate.next()
        override fun mutateChromosome(chromosome: Chromosome<T, G>) =
            chromosome.duplicateWithGenes(chromosome.reversed())
    }
}

/**
 * Creates an arbitrary generator for [BitFlipMutator]<[G]> instances with configurable mutation rates.
 *
 * This function, part of the [Arb.Companion] object, generates arbitrary instances of `BitFlipMutator<G>`.
 * A `BitFlipMutator` is a specific type of mutator used in genetic algorithms, particularly effective for genes
 * represented as [Boolean] values (binary genes). It operates by flipping the state of a gene (from true to false or
 * vice versa) based on given mutation rates. The [individualRate], [chromosomeRate], and [geneRate] parameters control
 * the probability of mutation at various levels – individual, chromosome, and gene, respectively.
 *
 * ## Functionality:
 * - Generates `BitFlipMutator` instances capable of flipping gene states in a binary chromosome.
 * - Mutation rates for individuals, chromosomes, and genes are specified by `individualRate`, `chromosomeRate`,
 *   and `geneRate`, with each being a probability ranging from 0.0 to 1.0.
 *
 * ## Usage:
 * This function is ideal for scenarios involving genetic algorithms with binary representation of genes. The
 * `BitFlipMutator` is particularly suited for problems where minor alterations (bit flips) can lead to significant
 * changes in an individual's fitness. It allows for fine-tuning of the mutation process across different levels of
 * genetic structure.
 *
 * ### Example:
 * ```kotlin
 * val bitFlipMutatorGen = Arb.bitFlipMutator<BooleanGene>()
 * val bitFlipMutator = bitFlipMutatorGen.bind() // Generates a BitFlipMutator instance with default mutation rates
 * // Use bitFlipMutator in a genetic algorithm to apply bit flip mutations
 * ```
 * In this example, `bitFlipMutatorGen` generates a `BitFlipMutator` instance. This mutator can be employed in a
 * genetic algorithm to introduce mutations by flipping the bits of Boolean genes, controlled by the specified
 * mutation rates.
 *
 * @param G The gene type, extending `Gene<Boolean, G>`.
 * @param individualRate An optional [Arb]<[Double]> for the individual mutation rate. Defaults to a probability value.
 * @param chromosomeRate An optional [Arb]<[Double]> for the chromosome mutation rate. Defaults to a probability value.
 * @param geneRate An optional [Arb]<[Double]> for the gene mutation rate. Defaults to a probability value.
 * @return An [Arb]<[BitFlipMutator]<[G]>> for generating `BitFlipMutator` instances with configurable mutation rates.
 */
fun <G> Arb.Companion.bitFlipMutator(
    individualRate: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    geneRate: Arb<Double> = Arb.probability(),
): Arb<BitFlipMutator<G>> where G : Gene<Boolean, G> = arbitrary {
    BitFlipMutator(individualRate.bind(), chromosomeRate.bind(), geneRate.bind())
}

/**
 * Creates an arbitrary generator for selecting a mutator with configurable mutation rates from a set of available
 * mutators.
 *
 * Part of the [Arb.Companion] object, this function facilitates the generation of either a [baseMutator] or
 * [bitFlipMutator], depending on the outcome of a random choice. This approach allows for dynamic mutation strategies
 * within genetic algorithms, especially useful when different types of mutation are desired across different runs or
 * scenarios.
 *
 * ## Functionality:
 * - Randomly selects between a `baseMutator` for [IntGene] and a `bitFlipMutator` for [BooleanGene].
 * - The `baseMutator` is configured with the provided [individualRate] and [chromosomeRate].
 * - The `bitFlipMutator` does not require specific rate configurations as it operates on binary genes.
 *
 * ## Usage:
 * This function is ideally used in scenarios where there is a need to employ different types of mutation strategies
 * in a genetic algorithm. By providing a choice between different mutators, it allows for a more varied and potentially
 * more effective evolutionary process.
 *
 * ### Example:
 * ```kotlin
 * val mutator = Arb.anyMutator()
 * // This will randomly select and create either a baseMutator or a bitFlipMutator
 * ```
 * In this example, `mutator` will be an instance of either `baseMutator<Int, IntGene>` or `bitFlipMutator<BooleanGene>`,
 * chosen randomly, providing varied mutation strategies within the genetic algorithm.
 *
 * @param individualRate An optional [Arb]<[Double]> for the individual mutation rate, used for `baseMutator`.
 *                       Defaults to a probability value.
 * @param chromosomeRate An optional [Arb]<[Double]> for the chromosome mutation rate, used for `baseMutator`.
 *                       Defaults to a probability value.
 * @return An arbitrary generator for either a `baseMutator` or `bitFlipMutator`, selected randomly.
 */
fun Arb.Companion.anyMutator(
    individualRate: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
) = choice(baseMutator<Int, IntGene>(individualRate, chromosomeRate))

