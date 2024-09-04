/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.MutationException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.utils.eq

/**
 * Interface representing a mutation operator in an evolutionary algorithm.
 *
 * The `Mutator` interface defines the contract for mutation operations applied to individuals in an evolutionary
 * algorithm. Mutation is a key genetic operator that introduces diversity by making small, random changes to an
 * individual's genes. This interface provides a flexible structure for implementing various mutation strategies.
 *
 * ## Theoretical Framework:
 * Mutation is a fundamental mechanism in evolutionary algorithms, inspired by the process of genetic mutation in
 * natural evolution. In biological systems, mutations occur at random, introducing variations into the genetic code of
 * organisms. These variations can lead to new traits that may be beneficial, neutral, or detrimental to the organism's
 * fitness.
 *
 * In the context of evolutionary algorithms, mutation serves a similar purpose: it introduces random variations into
 * the population, helping to maintain genetic diversity and preventing premature convergence to suboptimal solutions.
 * By altering the genes of individuals, mutation allows the algorithm to explore new regions of the solution space that
 * may not be reachable through crossover or selection alone. This exploration is crucial for finding global optima in
 * complex search spaces.
 *
 * The effectiveness of mutation depends on the mutation rates. The [individualRate] controls the probability that an
 * individual will be subject to mutation, while the [chromosomeRate] determines the likelihood of mutation occurring
 * within an individual's chromosomes. Setting these rates appropriately is key to balancing exploration and
 * exploitation in the evolutionary process. High mutation rates can lead to excessive randomness, disrupting the
 * convergence process, while low mutation rates may result in insufficient diversity, leading to premature convergence.
 *
 * ## Usage:
 * The recommended way to use the `Mutator` is through its [invoke] operator function, which mutates individuals in a
 * population according to the specified mutation rates. The other methods ([mutateIndividual] and [mutateChromosome])
 * are exposed as public to allow for fine-tuning and experimentation with new algorithms, but they should generally be
 * used within the context of the `invoke` function.
 *
 * ### Example: Using a Mutator in an Evolutionary Algorithm
 * ```kotlin
 * val mutator: Mutator<Int, MyGene> = MyMutator(individualRate = 0.1, chromosomeRate = 0.05)
 * val newState = mutator(state, state.population.size) { updatedPopulation ->
 *     state.copy(population = updatedPopulation)
 * }.getOrElse { throw it }
 * ```
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @property individualRate The probability that an individual will be subject to mutation.
 * @property chromosomeRate The probability that a chromosome within an individual will be subject to mutation.
 */
interface Mutator<T, G> : Alterer<T, G, Genotype<T, G>> where G : Gene<T, G> {

    /**
     * The probability that an individual will undergo mutation.
     */
    val individualRate: Double

    /**
     * The probability that a chromosome within an individual will undergo mutation.
     */
    val chromosomeRate: Double

    /**
     * Mutates a population of individuals, creating a new evolutionary state.
     *
     * This function applies the mutation process to a population, generating a new evolutionary state. The mutation
     * is applied based on the `individualRate` and `chromosomeRate`. If the `individualRate` is 0.0, no mutation
     * occurs, and the original state is returned.
     *
     * @param state The current evolutionary state.
     * @param outputSize The expected size of the output population. This must match the current population size.
     * @param buildState A function to build the new evolutionary state from the mutated individuals.
     * @return The new evolutionary state after mutation, or an error if the output size is invalid.
     * @throws MutationException if the output size is invalid or if an error occurs during mutation.
     */
    override suspend fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, G, Genotype<T, G>>>) -> S
    ): Either<MutationException, S> where S : EvolutionState<T, G, Genotype<T, G>, S> {
        constrained {
            "Output size ($outputSize) must match population size (${state.population.size})" {
                state.population must HaveSize(outputSize)
            }
        }.onLeft { return MutationException("Invalid output size: $outputSize", it).left() }
        return if (individualRate eq 0.0) {
            state.right()
        } else {
            state.map { if (Domain.random.nextDouble() > individualRate) it else mutateIndividual(it) }.right()
        }
    }

    /**
     * Mutates an individual by applying mutation to its chromosomes.
     *
     * This function is used internally by the `invoke` function to mutate an individual. The mutation is applied
     * to each chromosome of the individual based on the `chromosomeRate`.
     *
     * @param individual The individual to mutate.
     * @return The mutated individual.
     */
    fun mutateIndividual(individual: Individual<T, G, Genotype<T, G>>): Individual<T, G, Genotype<T, G>> =
        Individual(
            Genotype(individual.representation.chromosomes.map {
                if (Domain.random.nextDouble() > chromosomeRate) it else mutateChromosome(it)
            })
        )

    /**
     * Mutates a chromosome, producing a new chromosome.
     *
     * This function defines how a chromosome is mutated. The specific implementation of this function determines
     * the type and extent of mutation applied to the chromosome's genes.
     *
     * @param chromosome The chromosome to mutate.
     * @return The mutated chromosome.
     */
    fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G>
}
