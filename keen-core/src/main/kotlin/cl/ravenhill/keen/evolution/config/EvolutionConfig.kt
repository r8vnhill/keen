/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.ranking.FitnessRanker


/**
 * Configuration settings for the evolutionary algorithm.
 *
 * The `EvolutionConfig` data class encapsulates various configuration settings used to control the evolutionary
 * process. This includes the limits for stopping the process, the ranker for evaluating individuals, listeners for
 * monitoring events, the evaluator for assessing fitness, and the interceptor for pre- and post-processing states.
 *
 * ## Usage:
 * Use this class to configure the evolutionary algorithm with specific settings. This configuration is typically
 * passed to the evolutionary algorithm to define its behavior.
 *
 * ### Example:
 * ```kotlin
 * val listenerConfig = ...
 * val config = EvolutionConfig(
 *     limits = listOf(MaxGenerations(100, listenerConfig)),
 *     ranker = FitnessMaxRanker(),
 *     listeners = listOf(EvolutionSummary(listenerConfig)),
 *     evaluator = SequentialEvaluator(fitnessFunction),
 *     interceptor = EvolutionInterceptor.identity()
 * )
 * val algorithm = GeneticAlgorithm(config)
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property limits The list of limits used to determine when to stop the evolutionary process.
 * @property ranker The ranker used to evaluate and compare individuals in the population.
 * @property listeners The list of listeners that monitor and respond to events during the evolutionary process.
 * @property evaluator The evaluator used to assess the fitness of individuals in the population.
 * @property interceptor The interceptor used to apply pre- and post-processing to the evolutionary states.
 * @constructor Creates an instance of `EvolutionConfig` with the specified settings.
 */
data class EvolutionConfig<T, G>(
    val limits: List<Limit<T, G, Individual<T, G>>>,
    val ranker: FitnessRanker<T, G>,
    val listeners: List<EvolutionListener<T, G>>,
    val evaluator: EvaluationExecutor<T, G>,
    val interceptor: EvolutionInterceptor<T, G>,
) where G : Gene<T, G>
