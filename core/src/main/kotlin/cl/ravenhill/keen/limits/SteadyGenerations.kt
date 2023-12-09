/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord

/**
 * A limit based on the number of consecutive generations with no significant change in the fittest individual.
 *
 * `SteadyGenerations` is a termination criterion for evolutionary algorithms that stops the evolution process if
 * there has been no significant change in the fittest individual over a specified number of generations. It utilizes
 * an `EvolutionListener` to track the fittest individual of each generation and calculates the number of steady
 * generations.
 *
 * ## Key Features:
 * - **Generation Tracking**: Monitors the evolution process at the start and end of each generation.
 * - **Fittest Individual Analysis**: Determines the fittest individual in each generation based on a provided ranker.
 * - **Steady State Detection**: Calculates the number of consecutive generations where the fittest individual remains
 *   unchanged, indicating a potential steady state in the evolution.
 * - **Limit Check**: Triggers termination of the evolutionary process if the steady state persists for the specified
 *   number of generations.
 *
 * ## Usage:
 * Employ this class as a limit in evolutionary algorithms to prevent excessive computation when the population
 * appears to have converged or when there is a lack of sufficient genetic diversity to drive further changes.
 *
 * ### Example:
 * ```kotlin
 * val steadyLimit = SteadyGenerations<MyDataType, MyGene>(generations = 10)
 * // Use steadyLimit in an evolutionary algorithm setup
 * ```
 * In this example, `steadyLimit` is configured to stop the evolutionary process if there is no significant change
 * in the fittest individual for 10 consecutive generations.
 *
 * @param T The type of data encapsulated by the genes.
 * @param G The type of gene, conforming to the [Gene] interface.
 * @property generations The number of consecutive generations to check for steady state.
 */
class SteadyGenerations<T, G>(val generations: Int) : ListenLimit<T, G>(object : AbstractEvolutionListener<T, G>() {

    override fun onGenerationStarted(state: EvolutionState<T, G>) {
        currentGeneration = GenerationRecord(state.generation)
        currentGeneration.population.parents = List(state.population.size) {
            IndividualRecord(state.population[it].genotype, state.population[it].fitness)
        }
        evolution.generations += currentGeneration
    }

    override fun onGenerationEnded(state: EvolutionState<T, G>) {
        currentGeneration.population.offspring = List(state.population.size) {
            IndividualRecord(state.population[it].genotype, state.population[it].fitness)
        }
        currentGeneration.steady = EvolutionListener.computeSteadyGenerations(ranker, evolution)
    }
}, { evolution.generations.last().steady > generations }) where G : Gene<T, G> {

    init {
        constraints {
            "Number of steady generations [$generations] must be a positive integer" { generations must BePositive }
        }
    }
}
