/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration


/**
 * A class that limits the evolutionary computation process based on a target fitness value or a fitness predicate.
 * Once an individual in the population meets the specified fitness criterion, the evolution process will be stopped.
 *
 * ## Usage:
 *
 * Prefer using the [targetFitness] factory function to create instances of this class.
 *
 * This class extends `ListenLimit` and uses a listener to monitor the fitness of individuals in the population.
 *
 * ### Example 1: Creating a TargetFitness Limit with a Specific Fitness Value
 * ```
 * val targetFitness = TargetFitness(0.95)
 *
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += { TargetFitness(0.95) }
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * ### Example 2: Creating a TargetFitness Limit with a Predicate
 * ```
 * val targetFitness = TargetFitness { fitness -> fitness >= 0.95 }
 *
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += { TargetFitness { fitness -> fitness >= 0.95 } }
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property predicate the predicate to evaluate the fitness of individuals
 */
class TargetFitness<T, G>(val predicate: (Double) -> Boolean) : ListenLimit<T, G>(
    TargetFitnessListener(),
    { state -> state.population.any { predicate(it.fitness) } }
) where G : Gene<T, G> {

    override var engine: Evolver<T, G>? = null

    /**
     * Secondary constructor that initializes the `TargetFitness` with a specific fitness value.
     *
     * @param fitness the specific fitness value to target
     */
    constructor(fitness: Double) : this({ it == fitness })

    /**
     * Invokes the limit check to determine if any individual in the population meets the fitness predicate.
     *
     * @param state the current state of the evolution process
     * @return true if any individual meets the fitness predicate, false otherwise
     */
    override fun invoke(state: EvolutionState<T, G>) = state.population.any { predicate(it.fitness) }
}

/**
 * A private listener class used by `TargetFitness` to monitor the fitness of individuals in the population.
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
private class TargetFitnessListener<T, G> : AbstractEvolutionListener<T, G>() where G : Gene<T, G>

/**
 * Creates a factory function for `TargetFitness` that can be used to limit the evolutionary computation process based
 * on a fitness predicate. The factory function takes a `ListenerConfiguration` and returns a `TargetFitness` instance.
 *
 * ## Usage:
 * This function is a higher-order function that returns a factory function for creating `TargetFitness` objects.
 *
 * ### Example 1: Creating a TargetFitness Factory with a Specific Predicate
 * ```
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += targetFitness { fitness -> fitness >= 0.95 }
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param predicate the predicate to evaluate the fitness of individuals
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @return a factory function that takes a `ListenerConfiguration` and returns a `TargetFitness` instance
 */
fun <T, G> targetFitness(predicate: (Double) -> Boolean): (ListenerConfiguration<T, G>) -> ListenLimit<T, G>
        where G : Gene<T, G> = { TargetFitness(predicate) }

/**
 * Creates a factory function for `TargetFitness` that can be used to limit the evolutionary computation process based
 * on a specific fitness value. The factory function takes a `ListenerConfiguration` and returns a `TargetFitness`
 * instance.
 *
 * ## Usage:
 * This function is a higher-order function that returns a factory function for creating `TargetFitness` objects.
 *
 * ### Example 1: Creating a TargetFitness Factory with a Specific Fitness Value
 * ```
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += targetFitness(0.95)
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param fitness the specific fitness value to target
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @return a factory function that takes a `ListenerConfiguration` and returns a `TargetFitness` instance
 */
fun <T, G> targetFitness(fitness: Double): (ListenerConfiguration<T, G>) -> ListenLimit<T, G>
        where G : Gene<T, G> = { TargetFitness(fitness) }
