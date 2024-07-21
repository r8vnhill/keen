/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
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
 * class MyEvolutionListener<T, G>(configuration: ListenerConfiguration<T, G>) :
 *         AbstractEvolutionListener<T, G>(configuration) where G : Gene<T, G> {
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
 * @property configuration The configuration for the listener, providing access to common properties like ranker and
 * evolution record.
 * @see EvolutionListener for the interface this abstract class implements.
 * @see IndividualRanker for details on how individuals are ranked.
 * @see EvolutionRecord for details on recording evolution data.
 * @see TimeSource for details on timing various phases of the evolution process.
 */
abstract class AbstractEvolutionListener<T, G>(
    protected val configuration: ListenerConfiguration<T, G> = ListenerConfiguration()
) : EvolutionListener<T, G> where G : Gene<T, G> {

    @Deprecated("This property will be removed in future versions. Use configuration objects instead.")
    val ranker: IndividualRanker<T, G> = configuration.ranker

    @Deprecated("This property will be removed in future versions. Use configuration objects instead.")
    val evolution: EvolutionRecord<T, G> = configuration.evolution

    /**
     * The fittest individual in the current evolution process, determined using the ranker and evolution record from
     * the configuration.
     */
    val fittest: IndividualRecord<T, G>
        get() = fittest(configuration.ranker, configuration.evolution)
}
