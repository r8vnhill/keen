/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Determines the fittest individual from the most recent generation in the evolutionary record.
 *
 * The `fittest` function identifies the individual with the highest fitness value from the offspring population of the
 * most recent generation recorded in the provided [EvolutionRecord]. This function uses the given [IndividualRanker] to
 * sort the individuals based on their fitness and then selects the top individual.
 *
 * @param T The type of value held by the features within the individual.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param ranker The [IndividualRanker] used to evaluate and rank the individuals based on their fitness.
 * @param record The [EvolutionRecord] containing the evolutionary history, including all generations and their
 *   populations.
 * @return The fittest [IndividualRecord] from the most recent generation's offspring population.
 * @throws NoSuchElementException if the evolution record is empty or if the most recent generation has no offspring.
 */
suspend fun <T, F, R> fittest(
    ranker: IndividualRanker<T, F, R>,
    record: EvolutionRecord<T, F, R>
): IndividualRecord<T, F, R> where F : Feature<T, F>,
                                   R : Representation<T, F> {
    constrained {
        Jakt.shortCircuit = true // If evolution record is empty, generation check will not be executed
        "The evolution record must not be empty" { record.generations mustNot BeEmpty }
        "The most recent generation must have offspring" {
            record.generations.last().population.offspring mustNot BeEmpty
        }
    }
        .map { Jakt.shortCircuit = false }  // Reset the short-circuit flag
        .onLeft { throw it }
    return ranker.sort(
        record.generations
            .last().population
            .offspring
            .map { it.toIndividual() }
    ).first().let { IndividualRecord.fromIndividual(it) }
}
