/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.engines.Evolver
import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.genes.Gene


interface Limit<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    var engine: Evolver<T, F>?

    operator fun invoke(state: EvolutionState<T, F, R>): Boolean
}
