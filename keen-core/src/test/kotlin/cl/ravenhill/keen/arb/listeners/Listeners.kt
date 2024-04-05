/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.EvolutionListener
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.next
import kotlin.time.TestTimeSource
import kotlin.time.TimeSource

/**
 * Creates an arbitrary generator for [EvolutionListener]<[T], [G]> instances.
 *
 * This function, part of the [Arb.Companion] object, generates arbitrary instances of `EvolutionListener<T, G>`.
 * Each `EvolutionListener` instance is equipped with components essential for monitoring and reacting to
 * evolutionary processes, specifically an `IndividualRanker<T, G>`, an `EvolutionRecord<T, G>`, and a `TimeSource`.
 * The `ranker` and `evolution` components are generated using the provided arbitraries.
 *
 * ## Functionality:
 * - The `ranker` component is an `IndividualRanker<T, G>` instance responsible for ranking individuals based on
 *   their fitness. It is generated from the provided `ranker` arbitrary.
 * - The `evolution` component is an `EvolutionRecord<T, G>` instance that records the progress and results of
 *   evolutionary computations. It is generated from the provided `evolution` arbitrary.
 * - The `timeSource` component, set to a `TestTimeSource`, is used to track the time during the evolutionary process.
 *
 * ## Usage:
 * Utilize this arbitrary in scenarios involving genetic algorithms or simulations where monitoring and reacting to
 * the evolution process is required. It is particularly useful for testing or simulation purposes in evolutionary
 * computations.
 *
 * ### Example:
 * ```kotlin
 * val listenerGen = Arb.evolutionListener<Double, SomeGeneClass>(
 *     ranker = Arb.individualRanker(),
 *     evolution = Arb.evolutionRecord()
 * )
 * val listener = listenerGen.bind() // Generates an EvolutionListener instance
 * // Use listener in an evolutionary algorithm to monitor and record the evolution process
 * ```
 * In this example, `listener` is an `EvolutionListener` instance configured to monitor and record an evolutionary process.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param ranker An [Arb]<[IndividualRanker]<[T], [G]>> used to generate the ranker component of the listener.
 * @param evolution An [Arb]<[EvolutionRecord]<[T], [G]>> used to generate the evolution record component of the listener.
 * @return An [Arb]<[EvolutionListener]<[T], [G]>> that generates `EvolutionListener` instances.
 */
fun <T, G> arbEvolutionListener(
    ranker: Arb<IndividualRanker<T, G>>,
    evolution: Arb<EvolutionRecord<T, G>>,
): Arb<EvolutionListener<T, G>> where G : Gene<T, G> = arbitrary {
    object : EvolutionListener<T, G> {
        override var ranker: IndividualRanker<T, G> = ranker.next()
        override var evolution: EvolutionRecord<T, G> = evolution.next()
        override var timeSource: TimeSource = TestTimeSource()
    }
}
