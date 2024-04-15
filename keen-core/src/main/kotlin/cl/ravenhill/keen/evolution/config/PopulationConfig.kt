/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Encapsulates the configuration parameters for a population in an evolutionary algorithm.
 *
 * This class is used to configure essential aspects of a population, such as how individuals are generated
 * and the total number of individuals to maintain. It simplifies the process of passing these configurations
 * into components of the genetic algorithm, such as an evolutionary engine.
 *
 * Example Usage:
 * ```
 * val populationConfig = PopulationConfig(
 *     genotypeFactory = myGenotypeFactory,
 *     populationSize = 100
 * )
 * val evolutionEngine = EvolutionEngine(populationConfig, otherConfigParameters)
 * ```
 *
 * @param T The type of data encapsulated by the genes within the genotypes.
 * @param G The type of genes in the genotypes, conforming to the [Gene] interface.
 * @property genotypeFactory A factory responsible for generating the genotypes of individuals in the population.
 * @property populationSize The size of the population, indicating the total number of individuals.
 */
data class PopulationConfig<T, G>(
    val genotypeFactory: Genotype.Factory<T, G>,
    val populationSize: Int
) where G : Gene<T, G>
