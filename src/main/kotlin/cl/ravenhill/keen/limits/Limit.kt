/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.EvolutionListener

/**
 * Represents a termination criterion for genetic algorithms, known as a `Limit`.
 * This interface defines a function that accepts an [Evolver] instance and returns a boolean value
 * indicating whether the genetic algorithm should cease operation.
 *
 * A `Limit` can be implemented to encapsulate various stopping conditions tailored to specific
 * requirements of a genetic algorithm. Common examples of such conditions include halting the algorithm
 * after processing a predefined number of generations, achieving a certain fitness level, or when the
 * population exhibits signs of convergence.
 *
 * To utilize a `Limit`, implement this interface and specify the logic of the stopping condition
 * within the [invoke] method. The `invoke` method is called with the current state of the genetic
 * algorithm, represented by the [Evolver] instance, and it should return `true` to halt the algorithm
 * or `false` to allow it to continue.
 *
 * @param DNA The type representing the genetic data or information.
 * @param G The type of gene that the genetic algorithm operates on, which holds [DNA] type data.
 *
 * @property engine The [Evolver] instance that is executing the genetic algorithm.
 *
 * @see ListenLimit
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Limit<DNA, G> where G : Gene<DNA, G> {

    var engine: Evolver<DNA, G>?

    /**
     * Evaluates the stopping condition for the genetic algorithm based on the current state of the [Evolver].
     *
     * @param engine The [Evolver] instance running the genetic algorithm.
     * @return `true` if the algorithm should stop, otherwise `false`.
     */
    operator fun invoke(): Boolean
}


