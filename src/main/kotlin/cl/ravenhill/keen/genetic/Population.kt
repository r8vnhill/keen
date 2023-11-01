/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A typealias for a list of [Individual] objects representing a population of individuals.
 */
typealias Population<DNA, G> = List<Individual<DNA, G>>

/**
 * Returns a list of the fitness values of all the individuals in this population.
 *
 * The `fitness` property is a computed property that maps over all individuals in this population
 * and returns a list of their fitness values.
 *
 * @param DNA The type of the genetic data of the individuals.
 * @param G The type of the genes that make up the individuals.
 * @return A list of the fitness values of all the individuals in this population.
 */
val <DNA, G : Gene<DNA, G>> Population<DNA, G>.fitness: List<Double>
    get() = this.map { it.fitness }
