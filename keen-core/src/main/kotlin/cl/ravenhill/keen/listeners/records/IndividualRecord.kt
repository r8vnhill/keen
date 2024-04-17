/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a record of an individual in an evolutionary algorithm.
 *
 * This class serves as a simple data holder for an individual's genotype and fitness score. It provides a convenient
 * way to store and pass around these two key pieces of information without the additional functionalities or overhead
 * of the full `Individual` class.
 *
 * ## Key Components:
 * - **Genotype**: The genetic makeup of the individual, represented by a [Genotype].
 * - **Fitness**: A numerical value representing the fitness of the individual, typically indicating
 *    how well it performs in the context of the evolutionary algorithm.
 *
 * ## Usage:
 * `IndividualRecord` is particularly useful in scenarios where the genotype and fitness information needs
 * to be stored or processed separately from the `Individual` object, such as during logging, data analysis,
 * or when interfacing with external systems where the full `Individual` object is not required.
 *
 * ### Example:
 * ```
 * val genotype = Genotype(/* ... */)
 * val fitnessScore = calculateFitness(genotype)
 * val record = IndividualRecord(genotype, fitnessScore)
 * // Use the record for logging, analysis, etc.
 * ```
 * In this example, an `IndividualRecord` is created to hold the genotype and the calculated fitness score.
 * This record can then be used independently of the `Individual` object for various purposes.
 *
 * @param T The type of the genetic data or information.
 * @param G The type of gene encapsulated within the genotype.
 * @param genotype The genotype of the individual.
 * @param fitness The fitness score of the individual.
 *
 * @constructor Creates a new instance of `IndividualRecord` with the provided genotype and fitness score.
 *
 * @see Individual for the full representation of an individual in an evolutionary algorithm.
 */
class IndividualRecord<T, G>(val genotype: Genotype<T, G>, val fitness: Double) where G : Gene<T, G> {

    constructor(individual: Individual<T, G>) : this(individual.genotype, individual.fitness)

    /**
     * Converts this record into an `Individual` object.
     *
     * This method allows for the transformation of the lightweight `IndividualRecord` back into
     * a full-fledged `Individual` object, complete with all the functionalities and methods
     * associated with it. This is useful in scenarios where the full capabilities of `Individual`
     * are needed after working with the more streamlined `IndividualRecord`.
     *
     * @return An `Individual` object constructed from the genotype and fitness score stored in this record.
     */
    fun toIndividual() = Individual(genotype, fitness)
}
