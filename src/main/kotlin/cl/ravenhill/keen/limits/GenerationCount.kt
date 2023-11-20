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

/**
 * Represents a limit based on the number of generations evolved in the genetic algorithm.
 * This class serves as a stopping criterion for the evolution process, terminating the algorithm
 * after a specified number of generations have been completed.
 *
 * @param count The number of generations after which the evolution process should stop.
 *              Must be a positive integer.
 *
 * @constructor Creates a new instance of `GenerationCount` with the specified generation limit.
 *              The evolution process will terminate when the specified number of generations is reached.
 *
 * @see ListenLimit A base class for implementing limits based on listening to evolution events.
 * @see AbstractEvolutionListener A base class for creating listeners that respond to evolution events.
 *
 * @param DNA The type of the gene's value.
 * @param G The type of gene this listener will interact with.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
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


