/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.evolution.engines.Evolver
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration


/**
 * A class that limits the evolutionary computation process based on the maximum number of generations. Once the
 * specified number of generations is reached, the evolution process will be stopped.
 *
 * ## Usage:
 *
 * Prefer using the [maxGenerations] factory function to create instances of this class.
 *
 * This class extends `ListenLimit` and uses a listener to track the number of generations.
 *
 * ### Example 1: Creating a MaxGenerations Limit
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val maxGenerations = MaxGenerations(100, config)
 *
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += { c -> MaxGenerations(100, c) }
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property generations the maximum number of generations to run the evolution process
 * @property configuration the configuration for the listener
 */
data class MaxGenerations<T, G>(
    val generations: Int,
    val configuration: ListenerConfiguration<T, G> = ListenerConfiguration()
) : ListenLimit<T, G>(MaxGenerationsListener(configuration), { state -> state.generation >= generations }
) where G : Gene<T, G> {

    override var engine: Evolver<T, G>? = null
}

/**
 * A private listener class used by `MaxGenerations` to track the number of generations.
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @param configuration the configuration for the listener
 */
private class MaxGenerationsListener<T, G>(configuration: ListenerConfiguration<T, G>) :
    AbstractEvolutionListener<T, G>() where G : Gene<T, G> {
    var generation = 0
        private set

    /**
     * Called when a generation ends. Increments the generation count.
     *
     * @param state the current state of the evolution process
     */
    override fun onGenerationEnded(state: GeneticEvolutionState<T, G>) {
        generation++
    }
}

/**
 * Creates a factory function for `MaxGenerations` that can be used to limit the evolutionary computation process based
 * on the maximum number of generations. The factory function takes a `ListenerConfiguration` and returns a
 * `MaxGenerations` instance.
 *
 * ## Usage:
 * This function is a higher-order function that returns a factory function for creating `MaxGenerations` objects.
 *
 * ### Example 1: Creating a MaxGenerations Factory
 * ```
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += maxGenerations(100)
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param generations the maximum number of generations to run the evolution process
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @return a factory function that takes a `ListenerConfiguration` and returns a `MaxGenerations` instance
 */
fun <T, G> maxGenerations(generations: Int): (ListenerConfiguration<T, G>) -> MaxGenerations<T, G>
        where G : Gene<T, G> = { configuration -> MaxGenerations(generations, configuration) }
