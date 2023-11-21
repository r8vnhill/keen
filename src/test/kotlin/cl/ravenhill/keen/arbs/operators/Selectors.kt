package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.selector.RandomSelector
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.int

/**
 * Generates an arbitrary [RandomSelector].
 *
 * The [RandomSelector] is a simple selection mechanism that randomly selects individuals
 * from a population without considering their fitness.
 *
 * ## Usage Example:
 * ```kotlin
 * val selectorArb = Arb.randomSelector<Int, IntGene>()
 * val selector = selectorArb.bind() // Binding the arbitrary to get an instance
 * ```
 *
 * @return An [Arb] of [RandomSelector].
 */
fun <T, G> Arb.Companion.randomSelector() where G : Gene<T, G> = arbitrary {
    RandomSelector<T, G>()
}

/**
 * Generates an arbitrary [TournamentSelector] with a sample size.
 *
 * The [TournamentSelector] selects a subset of individuals from the population and chooses
 * the best individual from this subset, based on fitness.
 *
 * ## Usage Example:
 * ```kotlin
 * val sampleSizeArb = Arb.int(1..5)
 * val selectorArb = Arb.tournamentSelector<Int, IntGene>(sampleSizeArb)
 * val selector = selectorArb.bind() // Binding the arbitrary to get an instance
 * ```
 *
 * @param sampleSize An arbitrary generator for the size of the tournament.
 * @return An [Arb] of [TournamentSelector].
 */
fun <T, G> Arb.Companion.tournamentSelector(sampleSize: Arb<Int> = int(1..5)) where G : Gene<T, G> = arbitrary {
    TournamentSelector<T, G>(sampleSize.bind())
}

/**
 * Generates an arbitrary [RouletteWheelSelector].
 *
 * The [RouletteWheelSelector] implements a fitness-proportionate selection strategy,
 * where individuals are selected based on their fitness relative to the entire population.
 *
 * ## Usage Example:
 * ```kotlin
 * val sortedArb = Arb.boolean()
 * val selectorArb = Arb.rouletteWheelSelector<Int, IntGene>(sortedArb)
 * val selector = selectorArb.bind() // Binding the arbitrary to get an instance
 * ```
 *
 * @param sorted An arbitrary generator indicating whether the population is sorted or not.
 * @return An [Arb] of [RouletteWheelSelector].
 */
fun <T, G> Arb.Companion.rouletteWheelSelector(sorted: Arb<Boolean> = boolean()) where G : Gene<T, G> = arbitrary {
    RouletteWheelSelector<T, G>(sorted.bind())
}

/**
 * Generates an arbitrary [Selector] from a choice of different selector types.
 *
 * This function creates an arbitrary generator that randomly selects between different
 * types of selectors, such as [RandomSelector], [TournamentSelector], and [RouletteWheelSelector].
 *
 * ## Usage Example:
 * ```kotlin
 * val selectorArb = Arb.selector<Int, IntGene>()
 * val selector = selectorArb.bind() // Binding the arbitrary to get an instance
 * ```
 *
 * @return An [Arb] of [Selector].
 */
fun <T, G> Arb.Companion.selector() where G : Gene<T, G> =
    choice(randomSelector<T, G>(), tournamentSelector(), rouletteWheelSelector())
