package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.features.Feature

interface Evolver<T, F> where F : Feature<T, F> {

    fun evolve(): GeneticEvolutionState<T, G>
}
