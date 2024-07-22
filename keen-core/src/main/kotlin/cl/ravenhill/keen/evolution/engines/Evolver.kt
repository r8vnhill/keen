package cl.ravenhill.keen.evolution.engines

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener

interface Evolver<T, G> where G : Gene<T, G> {

    val listeners: MutableList<EvolutionListener<T, G>>

    /**
     * Executes the evolutionary algorithm until a specified termination condition is met.
     *
     * This function represents the main loop of the evolutionary algorithm, where generations are iterated through
     * until one or more termination conditions (limits) are satisfied. It manages the overall flow of the evolutionary
     * process, from the initial generation to the final state that meets the defined criteria.
     *
     * ## Evolutionary Loop:
     * 1. **Evolution Start Notification**: Notifies all registered listeners that the evolution process has started.
     * 2. **Generation Iteration**: Repeatedly iterates through generations using the `iterateGeneration` method.
     * 3. **Termination Check**: After each iteration, checks if any of the termination conditions (limits) are
     *   satisfied.
     * 4. **Evolution End Notification**: Once a termination condition is met, notifies all registered listeners that
     *   the evolution process has ended.
     *
     * ## Usage:
     * The `evolve` method is the entry point for executing the evolutionary algorithm. It is invoked when the
     * algorithm is ready to start and will continue to run until the specified termination conditions are met.
     *
     * ### Example:
     * ```kotlin
     * val engine = /* Create an instance of EvolutionEngine */
     * val finalState = engine.evolve()
     * // The finalState represents the state of the evolution at the end of the process
     * ```
     * In this example, `evolve` is called to start the evolutionary process. The method continues to iterate through
     * generations until a termination condition is satisfied, returning the final state of the evolution.
     *
     * @return The final [EvolutionState] after the termination conditions are met, representing the end of the
     *   evolutionary process.
     */
    fun evolve(): EvolutionState<T, G>
}