/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener


interface Evolver<T, G> where G : Gene<T, G> {

    val listeners: MutableList<EvolutionListener<T, G>>

    fun evolve(): EvolutionState<T, G>
}