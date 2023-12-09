/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.ranking.IndividualRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Generates an arbitrary [Selector] instance for property-based testing in evolutionary algorithms.
 *
 * This function creates instances of [Selector] suitable for use in testing scenarios involving evolutionary
 * algorithms. It provides a default implementation of the `select` ([Selector.select]) method that simply takes the
 * first `count` individuals from the population. This basic behavior is typically sufficient for testing purposes where
 * the focus is not on the selection logic itself but on other aspects of the evolutionary process.
 *
 * ## Usage:
 * This arbitrary generator can be used in property-based testing frameworks like Kotest to create [Selector] instances
 * for use in tests of evolutionary algorithms. It ensures that the selection process is consistent and predictable for
 * test scenarios.
 *
 * ### Example:
 * ```kotlin
 * val selectorArb = Arb.selector<MyDataType, MyGene>()
 * val selector = selectorArb.bind() // Instance of a Selector
 * // Use the selector in evolutionary algorithm testing
 * ```
 * In this example, `selectorArb` generates a simple [Selector] instance. When bound, `selector` can be used
 * in tests to simulate the selection process in an evolutionary algorithm.
 *
 * @param T The type of data encapsulated by the genes within the individuals.
 * @param G The type of gene in the individuals, conforming to the [Gene] interface.
 * @return An [Arb] that generates [Selector] instances with a simple selection logic.
 */
fun <T, G> Arb.Companion.selector(): Arb<Selector<T, G>> where G : Gene<T, G> = arbitrary {
    object : Selector<T, G> {
        override fun select(
            population: Population<T, G>,
            count: Int,
            ranker: IndividualRanker<T, G>,
        ) = population.take(count)
    }
}
