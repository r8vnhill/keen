/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutation

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeEqualTo
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alterers.Alterer
import cl.ravenhill.keen.utils.eq


/**
 * Defines a mutator for genetic algorithms, responsible for introducing variations in individuals' genotypes.
 *
 * `Mutator` is an [Alterer] that applies mutations to the genes of individuals in a population. It is a crucial
 * component in evolutionary algorithms, promoting genetic diversity and enabling the exploration of new areas in the
 * search space.
 *
 * ## Usage:
 * Implement this interface to define custom mutation logic. The mutation process can vary widely, from simple
 * alterations like flipping bits or changing numeric values, to complex structural changes in the genetic makeup.
 *
 * ### Example:
 * ```kotlin
 * class MyMutator : Mutator<MyDataType, MyGeneType> {
 *     override val probability = 0.1
 *     override val chromosomeRate = 0.5
 *
 *     override fun mutateChromosome(
 *         chromosome: Chromosome<MyDataType, MyGeneType>
 *     ): Chromosome<MyDataType, MyGeneType> {
 *         // Mutation logic for each chromosome
 *     }
 * }
 *
 * // Usage in evolutionary algorithm
 * val mutator = MyMutator()
 * val newState = mutator(currentState, outputSize)
 * ```
 * In this example, `MyMutator` defines custom mutation logic for chromosomes and genes. It is then applied to
 * the current state of the evolutionary process, potentially altering the genetic composition of individuals.
 *
 * @param T The type of data encapsulated by the genes.
 * @param G The type of gene, conforming to the [Gene] interface.
 * @property individualRate The probability of mutation occurring on an individual.
 * @property chromosomeRate The rate at which chromosomes are subjected to mutation within an individual.
 */
interface Mutator<T, G> : Alterer<T, G> where G : Gene<T, G> {
    val individualRate: Double
    val chromosomeRate: Double

    /**
     * Executes the mutation process on the population of an [EvolutionState].
     *
     * This method applies genetic mutations to individuals in a population based on the defined mutation probability
     * and chromosome rate. It ensures that the evolutionary process explores a diverse range of genetic variations by
     * introducing changes in the genotypes of individuals.
     *
     * ## Process:
     * 1. **Probability Check**: Skips mutation if the probability is set to 0.0, returning the current state unchanged.
     * 2. **Mutation Application**: For each individual in the population, a random check against the chromosome rate
     *    determines if the individual will be mutated. If the random check passes, the individual undergoes mutation.
     * 3. **Population Consistency**: Ensures the size of the mutated population matches the specified output size.
     *
     * ## Usage:
     * The method is typically invoked as part of the evolutionary cycle, altering the genetic composition of the
     * population to introduce variability and new genetic traits.
     *
     * ### Example:
     * ```kotlin
     * val mutator = MyMutator(probability = 0.1, chromosomeRate = 0.5)
     * val mutatedState = mutator(currentEvolutionState, outputSize)
     * ```
     * In this example, `MyMutator` is used to mutate the current population. The `invoke` method checks each individual
     * against the mutation probability and chromosome rate to decide whether to apply mutation.
     *
     * @param state The current evolution state containing the population.
     * @param outputSize The desired size of the population after mutation, typically equal to the current population
     *   size.
     * @return An [EvolutionState] containing the mutated population. The state will have the same generation number but
     *         potentially altered individuals.
     */
    override fun invoke(state: EvolutionState<T, G>, outputSize: Int): EvolutionState<T, G> {
        if (individualRate eq 0.0) return state
        val result = state.population.map {
            if (Domain.random.nextDouble() > chromosomeRate) {
                it
            } else {
                mutateIndividual(it)
            }
        }
        return EvolutionState(state.generation, state.ranker, result).apply {
            constraints {
                "The size of the population after mutation [$size] must be equal to the output size [$outputSize]" {
                    size must BeEqualTo(outputSize)
                }
            }
        }
    }

    /**
     * Applies mutation to the genotype of a given individual.
     *
     * This method is responsible for mutating the genetic structure of an individual. It iterates over each chromosome
     * in the individual's genotype and applies the [mutateChromosome] method to each one. The result is a new genotype
     * composed of these mutated chromosomes. The fitness of the resulting individual is set to `Double.NaN`, indicating
     * that it needs re-evaluation after mutation.
     *
     * ## Key Functionality:
     * - **Genotype Mutation**: Transforms each chromosome in the genotype through the `mutateChromosome` method,
     *   introducing genetic variations.
     * - **Creation of a New Individual**: Generates a new individual with the mutated genotype, ensuring that the
     *   original individual remains unaltered.
     * - **Fitness Re-Evaluation**: Sets the fitness of the new individual to `Double.NaN`, signaling that its fitness
     *   needs to be recalculated to reflect the genetic changes.
     *
     * ## Usage:
     * This method is utilized in genetic algorithms as part of the mutation process. It is especially important for
     * maintaining genetic diversity within the population and exploring new genetic possibilities.
     *
     * ### Example:
     * ```kotlin
     * val individual = /* Existing individual with a specific genotype */
     * val mutatedIndividual = mutator.mutateIndividual(individual)
     * // mutatedIndividual now contains a mutated version of the original genotype
     * ```
     * In this example, `mutateIndividual` is called on an existing individual, resulting in a new individual with a
     * mutated genotype. The fitness of this new individual will need to be evaluated to reflect its mutated genetic
     * makeup.
     *
     * @param individual The [Individual] whose genotype is subject to mutation.
     * @return A new [Individual] instance with a mutated genotype. The fitness value is reset to `Double.NaN`.
     */
    fun mutateIndividual(individual: Individual<T, G>): Individual<T, G> =
        Individual(Genotype(individual.genotype.map { mutateChromosome(it) }), Double.NaN)


    /**
     * Performs mutation on a given chromosome.
     *
     * This method is a critical part of the genetic mutation process in evolutionary algorithms. It takes a chromosome
     * as input and applies mutation logic to it, resulting in a new chromosome with altered genetic information. The
     * specifics of the mutation process depend on the implementation and can range from slight alterations to
     * significant changes in the genetic makeup of the chromosome.
     *
     * ## Key Functionality:
     * - **Chromosome Transformation**: Alters the genetic structure of the input chromosome. This could involve
     *   modifying one or more genes within the chromosome.
     * - **Genetic Diversity**: Introduces variations in the genetic material, contributing to the genetic diversity of
     *   the population. This is essential for exploring new genetic configurations and avoiding premature convergence
     *   to local optima.
     * - **Generation of New Chromosome**: Produces a new chromosome instance with the mutated genes, ensuring that the
     *   original chromosome remains unchanged.
     *
     * ## Usage:
     * This method is typically invoked as part of the mutation phase in a genetic algorithm, where it's applied to
     * chromosomes of individuals in the population. It plays a vital role in enabling genetic algorithms to search
     * effectively through the solution space.
     *
     * ### Example:
     * ```kotlin
     * val chromosome = /* An existing chromosome */
     * val mutatedChromosome = mutator.mutateChromosome(chromosome)
     * // mutatedChromosome now contains the mutated version of the original chromosome
     * ```
     * In this example, `mutateChromosome` is called on an existing chromosome, producing a new chromosome that carries
     * the mutations. This new chromosome can then be used to form a new individual or replace the existing one in the
     * population, depending on the evolutionary strategy.
     *
     * @param chromosome The [Chromosome] to be mutated.
     * @return A new [Chromosome] instance representing the result of the mutation. This instance differs from the
     *   original in its genetic composition.
     */
    fun mutateChromosome(chromosome: Chromosome<T, G>): Chromosome<T, G>
}
