/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.ranking.IndividualRanker


/**
 * Configuration for an evolutionary algorithm.
 *
 * The `EvolutionConfig` class encapsulates various parameters and components needed to configure an evolutionary
 * algorithm. This includes limits on the evolution process, a ranker for evaluating individuals, listeners for
 * monitoring evolution events, an evaluator for fitness calculations, and an interceptor for modifying the evolution
 * state.
 *
 * ## Usage:
 * This class is used to create a comprehensive configuration for an evolutionary algorithm. It is typically
 * constructed with specific implementations of the required components to tailor the evolutionary process to
 * particular needs.
 *
 * ### Example:
 * ```
 * val config = EvolutionConfig(
 *     limits = listOf(GenerationLimit(100)),
 *     ranker = MyIndividualRanker(),
 *     listeners = listOf(MyEvolutionListener()),
 *     evaluator = MyEvaluationExecutor(),
 *     interceptor = EvolutionInterceptor.identity()
 * )
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @property limits A list of limits to control the evolution process.
 * @property ranker The ranker used to evaluate individuals in the population.
 * @property listeners A list of listeners to monitor evolution events.
 * @property evaluator The evaluator used for fitness calculations.
 * @property interceptor The interceptor used to modify the evolution state before and after operations.
 * @constructor Creates an instance of `EvolutionConfig` with the specified parameters.
 */
data class EvolutionConfig<T, F, R>(
    val limits: List<Limit<T, F>>,
    val ranker: IndividualRanker<T, F>,
    val listeners: List<EvolutionListener<T, F>>,
    val evaluator: EvaluationExecutor<T, F>,
    val interceptor: EvolutionInterceptor<T, F, R>,
) where F : Feature<T, F>, R : Representation<T, F>
