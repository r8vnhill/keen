/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Represents a collection of individuals forming a population in an evolutionary algorithm.
 *
 * The `Population` typealias is a core concept in the field of evolutionary algorithms, representing a group of
 * individuals that undergo processes like selection, crossover, and mutation. Each individual in the population
 * embodies a potential solution to the problem being addressed by the algorithm.
 *
 * @param T The type of data encapsulated by the genes within the individuals' genotypes.
 * @param G The type of genes contained within the individuals' genotypes. These genes collectively form the genetic
 *   makeup of each individual in the population.
 */
typealias Population<T, G> = List<Individual<T, G>>

/**
 * An extension property to retrieve the fitness values of all individuals in a population.
 *
 * This property simplifies the process of accessing the fitness scores of each individual in a population.
 * It maps over the population, extracting the fitness value from each individual and compiling these values into a
 * list. This can be particularly useful for statistical analysis, such as calculating average fitness, identifying the
 * fittest individuals, or tracking fitness over generations in an evolutionary algorithm.
 *
 * Example Usage:
 * ```
 * val population: Population<MyDataType, MyGene> = /* Initialize population */
 * val fitnessScores = population.fitness
 * println("Average fitness: ${fitnessScores.average()}")
 * ```
 * In this example, the `fitness` property is used to retrieve the fitness scores of all individuals in the population,
 * and the average fitness score is calculated and printed.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @return A list of [Double] representing the fitness scores of each individual in the population.
 */
val <T, G> Population<T, G>.fitness: List<Double> where G : Gene<T, G>
    get() = this.map { it.fitness }
