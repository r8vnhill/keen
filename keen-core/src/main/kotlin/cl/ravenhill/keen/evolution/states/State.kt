package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FitnessEvaluable
import cl.ravenhill.keen.ranking.FitnessRanker
import cl.ravenhill.keen.repr.Representation

interface State<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    val size: Int

    val population: Population<T, F, R>

    val ranker: FitnessRanker<T, F>

    val generation: Int

    fun isEmpty(): Boolean
}
