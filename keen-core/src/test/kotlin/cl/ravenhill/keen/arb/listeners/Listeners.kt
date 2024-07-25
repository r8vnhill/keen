/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Creates an arbitrary generator for [EvolutionListener]<[T], [G]> instances.
 *
 * This function, generates arbitrary instances of `EvolutionListener<T, G>`.
 * Each `EvolutionListener` instance is equipped with components essential for monitoring and reacting to
 * evolutionary processes, specifically an `IndividualRanker<T, G>`, an `EvolutionRecord<T, G>`, and a `TimeSource`.
 * The `ranker` and `evolution` components are generated using the provided arbitraries.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @return An [Arb]<[EvolutionListener]<[T], [G]>> that generates `EvolutionListener` instances.
 */
fun <T, G> arbEvolutionListener(): Arb<EvolutionListener<T, G>> where G : Gene<T, G> = arbitrary {
    object : EvolutionListener<T, G> {
    }
}
