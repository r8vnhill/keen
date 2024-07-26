/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.genetic

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.list

/**
 * Generates an arbitrary instance of [Individual] for property-based testing in evolutionary algorithms.
 *
 * This function creates instances of [Individual] with customizable genotypes and fitness values. It is essential
 * for testing scenarios involving evolutionary algorithms, where individuals with specific genetic makeups and
 * fitness scores need to be generated for simulations or evaluations.
 *
 * ## Functionality:
 * - The [genotype] parameter specifies the arbitrary generator for the individual's genotype, determining its genetic
 *   composition.
 * - The [fitness] parameter provides an arbitrary generator for the fitness value of the individual, quantifying its
 *   suitability or performance within the evolutionary algorithm.
 *
 * ## Usage:
 * ### Generating an [Individual] with a custom genotype and fitness:
 * ```kotlin
 * val individualArb = Arb.individual(
 *     genotype = Arb.myGenotype(),  // Custom genotype generator
 *     fitness = Arb.double(0.0..10.0) // Fitness values in the range of 0.0 to 10.0
 * )
 * val individual = individualArb.bind() // Resulting Individual with the specified genotype and fitness
 * ```
 *
 * This function is particularly useful for creating diverse populations of individuals with varying genetic
 * structures and fitness levels. It allows for comprehensive testing of the evolutionary processes, such as
 * selection, crossover, and mutation.
 *
 * @param T The type representing the genetic data or information in the genes.
 * @param G The type of [Gene] contained within the individual's genotype.
 * @param genotype An [Arb]<[Genotype]<[T], [G]>> for generating the individual's genotype.
 * @param fitness An [Arb]<[Double]> for generating the individual's fitness value.
 *
 * @return An [Arb] that generates instances of [Individual] with the specified genotype and fitness configurations.
 */
fun <T, G> arbIndividual(
    genotype: Arb<Genotype<T, G>>,
    fitness: Arb<Double> = Arb.double().filterNot { it.isNaN() },
) where G : Gene<T, G> = arbitrary {
    Individual(genotype.bind(), fitness.bind())
}

/**
 * Generates an arbitrary population of individuals for property-based testing.
 *
 * This function creates a list of individuals, representing a population in an evolutionary algorithm.
 * The population is generated using a provided arbitrary generator for individuals ([individual]) and
 * can vary in size within the specified range ([size]). This function is particularly useful for testing
 * scenarios in evolutionary algorithms that involve operations on populations, such as selection, crossover,
 * and mutation.
 *
 * ## Usage:
 * This arbitrary generator can be employed in property-based testing frameworks like Kotest to create
 * diverse populations for various test cases, ensuring robustness and comprehensive testing of algorithms
 * that manipulate populations.
 *
 * ### Example:
 * Generating a population with a random number of individuals (up to 50):
 * ```kotlin
 * val individualArb = Arb.individual(MyGenotypeArb, Arb.double(0.0..1.0))
 * val populationArb = Arb.population(individualArb, 1..50)
 * val population = populationArb.bind() // Resulting population will have between 1 to 50 individuals
 * ```
 *
 * In this example, `populationArb` generates a population of individuals where each individual is created
 * using `individualArb`. The size of the population will be within the range of 1 to 50.
 *
 * @param T The type of data encapsulated by the genes in the individuals.
 * @param G The specific type of gene contained within the individuals.
 * @param individual An [Arb]<[Individual]<[T], [G]>> for generating individual instances.
 * @param size An [IntRange] specifying the possible size range of the population. Defaults to a range of 0 to 50.
 * @return An [Arb] that generates lists of [Individual] instances, representing populations of varying sizes.
 */
fun <T, G> arbPopulation(
    individual: Arb<Individual<T, G>>,
    size: IntRange = 0..25
): Arb<List<Individual<T, G>>> where G : Gene<T, G> =
    Arb.list(individual, size)
