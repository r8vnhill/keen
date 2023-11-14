/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.util.listeners.records.GenerationRecord

/**
 * A [Limit] that checks whether the genetic algorithm has reached the target fitness.
 *
 * @param fitness The target fitness to reach.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 1.0.0
 */
data class TargetFitness<DNA, G>(val fitness: Double) : ListenLimit<DNA, G>(
    object : AbstractEvolutionListener<DNA, G>() {
        override fun onGenerationFinished(population: Population<DNA, G>) {
            _currentGeneration = GenerationRecord(generation)
        }
    },
    {
        this.currentGeneration.population.resulting.any { it.fitness >= fitness }
    }
) where G : Gene<DNA, G>