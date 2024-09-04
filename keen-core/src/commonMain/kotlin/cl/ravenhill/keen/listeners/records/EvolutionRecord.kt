/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.BeMonotonicallyIncreasing
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Represents a record of the evolution process in an evolutionary algorithm, tracking the generations that occur.
 *
 * The `EvolutionRecord` stores a list of [GenerationRecord] instances, ensuring that there are no duplicate generations
 * and that the generations are ordered in a monotonically increasing manner. It also includes an [InitializationRecord]
 * to track initialization timing.
 *
 * @param T The type of value held by the features in the generations.
 * @param F The type of feature used in the representation, which must implement [Feature].
 * @param R The type of representation, which must implement [Representation].
 *
 * @property _generations The list of [GenerationRecord] instances that track each generation in the evolution.
 * @property initialization An instance of [InitializationRecord] that tracks the initialization timing.
 *
 * @throws CompositeException If the constraints on the generations are violated, such as duplicate generations or
 *   unordered generations.
 */
data class EvolutionRecord<T, F, R>(
    private val _generations: MutableList<GenerationRecord<T, F, R>> = mutableListOf()
) : AbstractTimedRecord() where F : Feature<T, F>, R : Representation<T, F> {

    init {
        constrained {
            "There should be no duplicate generations" {
                _generations.map { it.generation }.distinct() must HaveSize(_generations.size)
            }

            "Generations should be ordered" {
                _generations.map { it.generation } must BeMonotonicallyIncreasing()
            }
        }.onLeft { throw it }
    }

    /**
     * The list of [GenerationRecord] instances that track each generation in the evolution.
     */
    val generations: List<GenerationRecord<T, F, R>> = _generations

    /**
     * An [InitializationRecord] instance that tracks the timing of the initialization process.
     */
    val initialization = InitializationRecord()

    /**
     * Adds a new generation to the evolution record, ensuring that the new generation is greater than the last one.
     *
     * @param generation The new [GenerationRecord] to be added.
     * @throws CompositeException If the new generation does not follow the required ordering.
     */
    operator fun plusAssign(generation: GenerationRecord<T, F, R>) {
        constrained {
            "The generation should be greater than the last one" {
                (_generations.lastOrNull()?.generation ?: 0) must BeAtLeast(generation.generation + 1)
            }
        }.onLeft { throw it }
        _generations.add(generation)
    }

    /**
     * A record that tracks the initialization timing of the evolutionary process.
     */
    class InitializationRecord : AbstractTimedRecord()
}
