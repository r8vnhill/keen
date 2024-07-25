/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.ranking.FitnessRanker


data class EvolutionConfig<T, G>(
    val limits: List<Limit<T, G>>,
    val ranker: FitnessRanker<T, G>,
    val listeners: List<EvolutionListener<T, G>>,
    val evaluator: EvaluationExecutor<T, G>,
    val interceptor: EvolutionInterceptor<T, G>,
) where G : Gene<T, G>
