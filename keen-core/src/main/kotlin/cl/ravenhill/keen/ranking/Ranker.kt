package cl.ravenhill.keen.ranking

import cl.ravenhill.keen.features.Feature

interface Ranker<T, F> where F : Feature<T, F> {

    /**
     * Ranks the individuals in the population.
     *
     * @param population The population to rank.
     * @return The ranked population.
     */
    fun rank(population: List<Individual<T, F>>): List<Individual<T, F>>
}