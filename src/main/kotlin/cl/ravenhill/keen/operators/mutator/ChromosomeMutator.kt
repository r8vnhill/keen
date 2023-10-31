/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a mutator specifically designed for mutating chromosomes.
 *
 * This interface provides the necessary functionalities to apply mutation
 * operations on chromosomes, ensuring that the mutation strategy is consistent
 * with the behavior defined for the given gene type.
 *
 * @param DNA The type representing the genetic data or information.
 * @param G The type of gene that this chromosome mutator operates on.
 *
 * @see Mutator
 * @see Gene
 */
interface ChromosomeMutator<DNA, G> : Mutator<DNA, G> where G : Gene<DNA, G> {

    /**
     * Represents the rate at which chromosomes should be mutated.
     *
     * The value is a double between 0.0 and 1.0, where 0.0 indicates no mutation and
     * 1.0 indicates a 100% chance of mutation.
     */
    override val chromosomeRate: Double

    /**
     * Mutates the provided chromosome based on the implemented mutation strategy.
     *
     * Implementations should define the specifics of the mutation strategy, ensuring
     * that the mutation respects the defined chromosome rate.
     *
     * @param chromosome The chromosome to be mutated. Represents the genetic structure
     *                   to be potentially altered by the mutation process.
     * @return [MutatorResult] encapsulating the original chromosome, the mutated chromosome,
     *         and any other mutation-related data, allowing for tracking and analysis
     *         of the mutation results.
     */
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>
    ): MutatorResult<DNA, G, Chromosome<DNA, G>>
}
