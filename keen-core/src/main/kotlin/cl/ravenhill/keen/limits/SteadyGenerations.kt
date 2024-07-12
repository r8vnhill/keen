/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mapGeneration
import cl.ravenhill.keen.listeners.mixins.GenerationListener
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord

/**
 * A class that tracks the number of steady generations in the evolutionary computation process. A steady generation is
 * one where the fitness of the fittest individual does not change. This class uses a listener to monitor the start and
 * end of generations and updates the steady generation count accordingly.
 *
 * ## Usage:
 * This class extends `ListenLimit` and implements `GenerationListener` to handle events occurring at the start and end of each generation,
 * and records relevant information about steady generations.
 *
 * ### Example 1: Using SteadyGenerations in an Evolution Engine
 * ```
 * fun main() {
 *     val engine = evolutionEngine(::schaffer2, genotypeOf {
 *         chromosomeOf {
 *             doubles {
 *                 ranges += -100.0..100.0
 *                 size = 2
 *             }
 *         }
 *     }) {
 *         ranker = FitnessMinRanker()
 *         populationSize = 500
 *         parentSelector = TournamentSelector()
 *         survivorSelector = TournamentSelector()
 *         alterers += listOf(RandomMutator(0.1), AverageCrossover(0.3))
 *         listeners += listOf(EvolutionSummary(), EvolutionPlotter())
 *         limits += listOf(SteadyGenerations(50), MaxGenerations(500))
 *     }
 *     engine.evolve()
 *     engine.listeners.forEach { it.display() }
 * }
 * ```
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @property generations the number of steady generations to track
 */
class SteadyGenerations<T, G>(
    val generations: Int,
    configuration: ListenerConfiguration<T, G> = ListenerConfiguration()
) : ListenLimit<T, G>(
    object : AbstractEvolutionListener<T, G>(), GenerationListener<T, G> by object : GenerationListener<T, G> {

        private val currentGeneration = configuration.currentGeneration
        private val evolution = configuration.evolution
        private val ranker = configuration.ranker

        /**
         * Called when a generation starts. Initializes a new generation record and updates the parents' information.
         *
         * @param state the current state of the evolution process
         */
        override fun onGenerationStarted(state: EvolutionState<T, G>) {
            currentGeneration.value = GenerationRecord(state.generation)
            mapGeneration(currentGeneration) {
                population.parents = List(state.population.size) {
                    IndividualRecord(state.population[it].genotype, state.population[it].fitness)
                }
            }
            evolution.generations += currentGeneration.value!!
        }

        /**
         * Called when a generation ends. Updates the current generation record with the offspring information and the number of steady generations.
         *
         * @param state the current state of the evolution process
         */
        override fun onGenerationEnded(state: EvolutionState<T, G>) {
            mapGeneration(currentGeneration) {
                population.offspring = List(state.population.size) {
                    IndividualRecord(state.population[it].genotype, state.population[it].fitness)
                }
                steady = EvolutionListener.computeSteadyGenerations(ranker, evolution)
            }
        }
    } {
    }, { evolution.generations.last().steady > generations }) where G : Gene<T, G> {

    init {
        constraints {
            "Number of steady generations [$generations] must be a positive integer" { generations must BePositive }
        }
    }
}
