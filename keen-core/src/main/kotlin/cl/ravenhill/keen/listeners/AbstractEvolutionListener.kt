/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import kotlin.time.TimeSource


/**
 * An abstract base class for implementing an evolution listener in a genetic algorithm.
 *
 * `AbstractEvolutionListener` provides a skeletal implementation of the `EvolutionListener` interface. It is designed
 * to be extended by classes that need to monitor the evolutionary process. This abstract class pre-defines and
 * initializes common properties used by most implementations, reducing the boilerplate code needed in concrete
 * listener classes.
 *
 * ## Usage:
 * Extend this class to create a custom evolution listener. Override the callback methods of `EvolutionListener` as
 * needed to implement specific behaviors at different stages of the evolutionary process.
 *
 * ### Example:
 * ```kotlin
 * class MyEvolutionListener<T, G> : AbstractEvolutionListener<T, G>() where G : Gene<T, G> {
 *     override fun onGenerationEnded(state: EvolutionState<T, G>) {
 *         // Custom logic for handling the end of a generation
 *     }
 * }
 * ```
 * In this example, `MyEvolutionListener` extends `AbstractEvolutionListener` and provides a custom implementation for
 * the `onGenerationEnded` method.
 *
 * @param T The type of data used in the evolution process, typically representing the genetic information.
 * @param G The type of gene used in the evolution process, must be a subtype of [Gene].
 * @see EvolutionListener for the interface this abstract class implements.
 * @see IndividualRanker for details on how individuals are ranked.
 * @see EvolutionRecord for details on recording evolution data.
 * @see TimeSource for details on timing various phases of the evolution process.
 */
abstract class AbstractEvolutionListener<T, G> : EvolutionListener<T, G> where G : Gene<T, G> {
    val fittest: IndividualRecord<T, G>
        get() = ranker.sort(evolution.generations.last().population.offspring.map { it.toIndividual() }).first()
            .let { IndividualRecord(it.genotype, it.fitness) }
}
