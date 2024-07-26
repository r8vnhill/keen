package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.ranking.Ranker
import cl.ravenhill.keen.repr.Representation

interface State<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    val size: Int

    val population: Population<T, F, R>

    val ranker: Ranker

    val generation: Int

    fun isEmpty(): Boolean
}
