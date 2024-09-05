/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.genetics.genotype.GenotypeFactory

/**
 * Configuration for creating and managing the population in a genetic algorithm.
 *
 * The `GeneticPopulationConfiguration` class encapsulates the settings needed to initialize and manage a population
 * of genotypes in an evolutionary algorithm. It includes a [GenotypeFactory], responsible for generating new genotypes,
 * and the size of the population to be maintained.
 *
 * @param T The type of value held by the genes in the genotype.
 * @param G The type of gene within the genotype, which must extend [Gene].
 * @property genotypeFactory A [GenotypeFactory] responsible for generating genotypes in the population.
 * @property populationSize The size of the population to be generated or maintained.
 */
data class GeneticPopulationConfiguration<T, G>(
    val genotypeFactory: GenotypeFactory<T, G>,
    val populationSize: Int
) where G : Gene<T, G>
