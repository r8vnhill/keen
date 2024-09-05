/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.genetics.genotype.Genotype
import cl.ravenhill.keen.operators.alteration.Alterer

/**
 * Configuration for applying alterations to genotypes in an evolutionary algorithm.
 *
 * The `AlterationConfiguration` class encapsulates a list of [Alterer] instances, which are responsible for modifying
 * genotypes during the evolutionary process. Alterers can perform various genetic operations, such as mutation,
 * crossover, or any other genetic modification. This configuration is used to apply these alterations in the
 * evolutionary cycle.
 *
 * @param T The type of value held by the genes in the genotype.
 * @param G The type of gene within the genotype, which must extend [Gene].
 * @property alterers The list of [Alterer] instances responsible for modifying the genotypes.
 */
data class AlterationConfiguration<T, G>(val alterers: List<Alterer<T, G, Genotype<T, G>>>) where G : Gene<T, G>
