package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener

interface Evolver<T, F> where F : Feature<T, F> {

    val listeners: MutableList<EvolutionListener<T, F>>

    fun evolve(): EvolutionState<T, F>
}
