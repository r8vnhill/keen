/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
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

