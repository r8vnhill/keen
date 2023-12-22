/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.selection.Selector

/**
 * Represents the configuration for selection processes in an evolutionary algorithm.
 *
 * This class encapsulates parameters related to the selection of individuals in a population,
 * including the rate of survival and the methods for selecting parents and survivors.
 *
 * This configuration is essential in controlling how individuals are selected for reproduction and survival,
 * impacting the genetic diversity and fitness of the population over generations. It allows for flexibility
 * in defining different selection strategies within the evolutionary process.
 *
 * Example Usage:
 * ```
 * val selectionConfig = SelectionConfig(
 *     survivalRate = 0.5,
 *     parentSelector = TournamentSelector(),
 *     survivorSelector = RouletteWheelSelector()
 * )
 * val evolutionEngine = EvolutionEngine(populationConfig, selectionConfig, otherConfigParameters)
 * ```
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @property survivalRate A double value representing the proportion of the population that will survive to the next
 *   generation.
 * @property parentSelector A [Selector] instance used for choosing parents for producing offspring.
 * @property survivorSelector A [Selector] instance used for choosing individuals that will survive to the next
 *   generation.
 */
data class SelectionConfig<T, G>(
    val survivalRate: Double,
    val parentSelector: Selector<T, G>,
    val survivorSelector: Selector<T, G>
) where G : Gene<T, G>
