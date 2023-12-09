/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alterers.Alterer

/**
 * Configuration class for specifying genetic alteration operations in an evolutionary algorithm.
 *
 * This class encapsulates a list of genetic operators, known as [Alterer]s, which are applied to the individuals
 * in a population. These alterers typically include operations such as mutation and crossover, which introduce
 * genetic variation and drive the evolution process.
 *
 * The `AlterationConfig` allows for the flexible composition of genetic operations, enabling customization of
 * the evolutionary process to suit different problem domains and requirements.
 *
 * Example Usage:
 * ```
 * val alterationConfig = AlterationConfig(
 *     RandomMutator(probability = 0.1),
 *     AverageCrossover()
 * )
 * val evolutionEngine = EvolutionEngine(populationConfig, selectionConfig, alterationConfig, evolutionConfig)
 * ```
 *
 * In this example, `AlterationConfig` is constructed with a random mutator and an average crossover alterer,
 * defining the genetic operations to be applied during the evolution. This configuration is then passed to
 * an `EvolutionEngine`, integrating it into the evolutionary process.
 */
data class AlterationConfig<T, G>(val alterers: List<Alterer<T, G>>) where G : Gene<T, G> {
    /**
     * Secondary constructor to conveniently initialize the configuration with a variable number of alterers.
     *
     * @param alterers A variable number of [Alterer] instances, each representing a specific genetic operation.
     */
    constructor(vararg alterers: Alterer<T, G>) : this(alterers.toList())
}
