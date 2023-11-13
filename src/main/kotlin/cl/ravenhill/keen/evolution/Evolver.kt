/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.listeners.Listeners

/**
 * Defines the core functionality of an evolutionary process.
 * An `Evolver` is capable of taking a population through successive generations, applying genetic
 * operators to produce new generations, and ultimately progressing towards an optimal or satisfactory
 * solution based on the genetic material type `DNA`.
 *
 * @param DNA The data type representing the genetic information of the population. It acts as a blueprint
 *            for creating individuals within the population.
 * @param G The concrete type of [Gene] that carries the genetic information (`DNA`). Genes are the
 *          fundamental units of heredity and are subject to evolutionary processes such as mutation
 *          and recombination.
 *
 * @property generation An integer representing the current generation number in the evolutionary process.
 *           This is typically used to track the progress and to apply generational limits.
 * @property steadyGenerations The number of generations that the population has remained unchanged.
 *           This is typically used to track the progress and to apply generational limits.
 * @property bestFitness The fitness of the best individual in the population.
 *           This is typically used to track the progress and to apply fitness limits.
 *
 * @return [EvolutionResult] encapsulating the final state of the population at the end of the evolution
 *         process. This includes the last generation evolved and any additional results or metadata.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Evolver<DNA, G : Gene<DNA, G>> {

    val generation: Int

    val steadyGenerations: Int

    val bestFitness: Double

    val listeners: MutableList<EvolutionListener<DNA, G>>

    /**
     * Initiates and conducts the evolutionary process. The method orchestrates the selection,
     * crossover, mutation, and evaluation steps, guiding the population towards improved fitness
     * over successive generations.
     *
     * The method continues the evolutionary process until a specified termination condition is met,
     * such as a maximum number of generations or a satisfactory fitness level.
     *
     * @return An [EvolutionResult] that holds the final generation of the evolution along with
     *         any pertinent information.
     */
    fun evolve(): EvolutionResult<DNA, G>
}
