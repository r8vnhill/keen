/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.LimitConfigurationException
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord

/**
 * A class that limits the evolutionary computation process based on the number of steady generations.
 * A steady generation is one where the fitness of the fittest individual does not change.
 * Once the specified number of steady generations is reached, the evolution process will be stopped.
 *
 * ## Usage:
 * This class extends `ListenLimit` and uses an inner listener to monitor the start and end of generations,
 * updating the steady generation count accordingly.
 *
 * ### Example 1: Creating a SteadyGenerations Limit
 * ```
 * val config = ListenerConfiguration<Int, MyGene>()
 * val steadyGenerations = SteadyGenerations(10, config)
 *
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += { c -> SteadyGenerations(10, c) }
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property generations the number of steady generations to track
 * @param configuration the configuration for the listener
 * @throws CompositeException if the number of steady generations is not a positive integer
 * @throws LimitConfigurationException if the number of steady generations is not a positive integer and
 *  [Jakt.shortCircuit] is `true`.
 */
class SteadyGenerations<T, G>(
    val generations: Int,
    configuration: ListenerConfiguration<T, G> = ListenerConfiguration()
) : ListenLimit<T, G>(
    object : AbstractEvolutionListener<T, G>() {

        private val currentGeneration = configuration.currentGeneration
        private val _evolution = configuration.evolution
        private val _ranker = configuration.ranker

        /**
         * Called when a generation starts. Initializes a new generation record and updates the parents' information.
         *
         * @param state the current state of the evolution process
         */
        override fun onGenerationStarted(state: GeneticEvolutionState<T, G>) {
            currentGeneration.value = GenerationRecord(state.generation)
            mapGeneration(currentGeneration) {
                population.parents = List(state.population.size) {
                    IndividualRecord(state.population[it].genotype, state.population[it].fitness)
                }
            }
            _evolution.generations += currentGeneration.value!!
        }

        /**
         * Called when a generation ends. Updates the current generation record with the offspring information and the
         * number of steady generations.
         *
         * @param state the current state of the evolution process
         */
        override fun onGenerationEnded(state: GeneticEvolutionState<T, G>) {
            mapGeneration(currentGeneration) {
                population.offspring = List(state.population.size) {
                    IndividualRecord(state.population[it].genotype, state.population[it].fitness)
                }
                steady = EvolutionListener.computeSteadyGenerations(_ranker, _evolution)
            }
        }
    }, { configuration.evolution.generations.last().steady > generations }) where G : Gene<T, G> {

    init {
        constraints {
            "Number of steady generations [$generations] must be a positive integer"(::LimitConfigurationException) {
                generations must BePositive
            }
        }
    }
}

/**
 * Creates a factory function for `SteadyGenerations` that can be used to limit the evolutionary computation process
 * based on the number of steady generations. The factory function takes a `ListenerConfiguration` and returns a
 * `SteadyGenerations` instance.
 *
 * ## Usage:
 * This function is a higher-order function that returns a factory function for creating `SteadyGenerations` objects.
 *
 * ### Example 1: Creating a SteadyGenerations Factory
 * ```
 * val engine = evolutionEngine(/* ... */) {
 *     limitFactories += steadyGenerations(10)
 *     // ...
 * }
 * engine.evolve()
 * ```
 *
 * @param generations the number of steady generations to track
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @return a factory function that takes a `ListenerConfiguration` and returns a `SteadyGenerations` instance
 */
fun <T, G> steadyGenerations(generations: Int): (ListenerConfiguration<T, G>) -> SteadyGenerations<T, G>
        where G : Gene<T, G> = { SteadyGenerations(generations, it) }
