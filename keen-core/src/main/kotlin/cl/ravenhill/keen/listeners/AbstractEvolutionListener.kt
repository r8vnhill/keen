/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import kotlin.time.ExperimentalTime
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
 * @property ranker An [IndividualRanker] instance used to rank individuals in the evolutionary process. By default, it
 *   is initialized to [FitnessMaxRanker], which ranks individuals based on their fitness with the highest fitness being
 *   the most preferred.
 * @property evolution An [EvolutionRecord] instance that records detailed information about the evolutionary process,
 *   such as generation timelines and individual records.
 * @property generations A lazy-initialized property providing convenient access to the list of generations recorded in
 *   [evolution].
 * @property currentGeneration Holds information about the current generation. It is marked as `lateinit` because it
 *   gets populated during the evolutionary process.
 * @see EvolutionListener for the interface this abstract class implements.
 * @see IndividualRanker for details on how individuals are ranked.
 * @see EvolutionRecord for details on recording evolution data.
 * @see TimeSource for details on timing various phases of the evolution process.
 */
abstract class AbstractEvolutionListener<T, G> : EvolutionListener<T, G> where G : Gene<T, G> {
    override var ranker: IndividualRanker<T, G> = FitnessMaxRanker()
    override var evolution: EvolutionRecord<T, G> = EvolutionRecord()
    protected val generations by lazy { evolution.generations }
    protected lateinit var currentGeneration: GenerationRecord<T, G>
    @ExperimentalTime
    override var timeSource: TimeSource = TimeSource.Monotonic
    val fittest: IndividualRecord<T, G>
        get() = ranker.sort(evolution.generations.last().population.offspring.map { it.toIndividual() }).first()
            .let { IndividualRecord(it.genotype, it.fitness) }
}
