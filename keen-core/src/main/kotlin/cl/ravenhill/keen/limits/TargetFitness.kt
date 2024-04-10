/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * A class representing a fitness-based termination condition in an evolutionary algorithm.
 *
 * `TargetFitness` is a specialized limit condition that checks whether any individual in the population
 * meets a specified fitness criterion. The fitness criterion is defined by a predicate function that
 * operates on the fitness value of individuals. When an individual meeting the criterion is found, the
 * condition is satisfied, which can trigger the termination of the evolutionary process.
 *
 * ## Usage:
 * This class is used in evolutionary algorithms to define a termination condition based on achieving a
 * target fitness level. It is particularly useful when the goal is to find individuals that meet or exceed
 * a certain fitness threshold.
 *
 * ### Example:
 * ```kotlin
 * // Terminate evolution when an individual with a fitness of exactly 10.0 is found
 * val targetFitness = TargetFitness<MyDataType, MyGene>(10.0)
 * val evolver = EvolutionEngine<MyDataType, MyGene>(/* ... */)
 * ```
 * In this example, the evolution process will terminate when any individual with a fitness of exactly 10.0 is found.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @param predicate A function that takes a fitness value (Double) and returns a Boolean indicating
 *   whether the condition is met.
 * @property engine An optional [Evolver] engine that the limit condition can reference. It is initially `null`
 *   and can be set by the evolutionary framework when used.
 * @constructor Creates a `TargetFitness` instance using a specific predicate.
 */
class TargetFitness<T, G>(val predicate: (Double) -> Boolean) : Limit<T, G> where G : Gene<T, G> {
    override var engine: Evolver<T, G>? = null

    /**
     * Secondary constructor for the `TargetFitness` class, creating an instance with a specific target fitness value.
     *
     * This constructor simplifies the creation of a `TargetFitness` instance by allowing the specification of a fixed
     * fitness value. It initializes the `TargetFitness` object with a predicate that checks if an individual's fitness
     * is equal to the provided fitness value. This is useful for scenarios where the termination condition is based on
     * finding an individual that exactly matches a given fitness value.
     *
     * ### Example:
     * ```kotlin
     * // Create a TargetFitness instance to terminate evolution when an individual with a fitness of exactly 10.0 is
     * // found
     * val targetFitness = TargetFitness<MyDataType, MyGene>(10.0)
     * ```
     * In this example, the `TargetFitness` instance is initialized to trigger termination when an individual with a
     * fitness value of exactly 10.0 is encountered in the population.
     *
     * @param fitness The target fitness value to be matched for the termination condition.
     */
    constructor(fitness: Double) : this({ it == fitness })

    /**
     * Evaluates the termination condition against the current state of the evolutionary process.
     *
     * This function is an implementation of the `invoke` method defined in the `Limit` interface ([Limit.invoke]). It
     * checks whether any individual in the population satisfies the fitness predicate defined in `TargetFitness`. If
     * at least one individual meets the condition (i.e., the predicate returns `true` for their fitness value),
     * this method returns `true`, indicating that the termination condition has been met.
     *
     * The method is typically called by the evolutionary framework at each generation to determine if the
     * evolutionary process should be terminated based on the target fitness criterion.
     *
     * ### Example:
     * ```kotlin
     * val targetFitness = TargetFitness<MyDataType, MyGene>({ it >= 15.0 })
     * val state: EvolutionState<MyDataType, MyGene> = /* current state of the evolution */
     *
     * if (targetFitness(state)) {
     *     println("Termination condition met: At least one individual has a fitness >= 15.0")
     * }
     * ```
     * In this example, the `invoke` method checks if any individual in the population has a fitness value of 15.0 or
     * greater.
     * If such an individual exists, the evolutionary process can be terminated.
     *
     * @param state The current state of the evolutionary process, containing the population to be checked.
     * @return `true` if the termination condition is met, `false` otherwise.
     */
    override fun invoke(state: EvolutionState<T, G>) = state.population.any { predicate(it.fitness) }
}
