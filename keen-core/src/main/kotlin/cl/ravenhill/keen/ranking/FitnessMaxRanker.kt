/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.FitnessEvaluable


class FitnessMaxRanker<T, F> : FitnessRanker<T, F> where F : Feature<T, F> {

    override fun invoke(first: FitnessEvaluable, second: FitnessEvaluable) = first.fitness.compareTo(second.fitness)

    override fun toString() = "FitnessMaxRanker"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FitnessMaxRanker<*, *>) return false
        return true
    }

    override fun hashCode() = FitnessMaxRanker::class.hashCode()
}
