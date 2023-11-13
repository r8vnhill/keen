/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.AbstractEvolutionListener

data class GenerationCount<DNA, G>(val count: Int) :
    ListenLimit<DNA, G>(object : AbstractEvolutionListener<DNA, G>() {
        override fun onGenerationFinished(population: Population<DNA, G>) {
            generation++
        }
    }, { generation >= count })
      where G : Gene<DNA, G> {

    init {
        constraints { "Generation count [$count] must be at least 1" { count must BePositive } }
    }
}

