/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.ranking.Ranker
import cl.ravenhill.keen.repr.Representation


/**
 * Configuration class for the evolution process in an evolutionary algorithm.
 *
 * The `EvolutionConfig` data class encapsulates various settings and components used to configure the evolutionary
 * process. This includes limits, rankers, listeners, evaluators, and interceptors.
 *
 * ## Usage:
 * This class is used to configure the evolutionary algorithm by specifying the necessary components and settings.
 *
 * ### Example 1: Basic Configuration
 * ```kotlin
 * val config = EvolutionConfig(
 *     limits = listOf(MyLimit()),
 *     ranker = MyRanker(),
 *     listeners = listOf(MyListener()),
 *     evaluator = MyEvaluator(),
 *     interceptor = EvolutionInterceptor.identity()
 * )
 * ```
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 * @param S The type of the evolutionary state, which must extend [EvolutionState].
 * @property limits The list of limits that define the stopping criteria for the evolutionary process.
 * @property ranker The ranker used to evaluate and compare individuals in the population.
 * @property listeners The list of listeners that respond to events during the evolutionary process.
 * @property evaluator The evaluator responsible for evaluating the fitness of individuals in the population.
 * @property interceptor The interceptor used to perform actions before and after each step of the evolution process.
 * @constructor Creates an instance of `EvolutionConfig` with the specified components and settings.
 */
data class EvolutionConfig<T, F, R, S>(
    val limits: List<Limit<T, F, Individual<T, F, R>>>,
    val ranker: Ranker<T, F, R>,
    val listeners: List<EvolutionListener<T, F, Individual<T, F, R>>>,
    val evaluator: EvaluationExecutor<T, F, R>,
    val interceptor: EvolutionInterceptor<T, F, R, S>,
) where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R>
