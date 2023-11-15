package cl.ravenhill.keen.arbs.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionPrinter
import cl.ravenhill.keen.util.listeners.EvolutionSummary
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.positiveInt

/**
 * Provides an arbitrary generator for creating instances of various [EvolutionListener] types,
 * useful for property-based testing in genetic algorithm scenarios.
 *
 * This function takes advantage of Kotest's property-based testing features to randomly generate
 * different implementations of the [EvolutionListener] interface. Each listener type provides
 * unique functionalities to observe and analyze the evolutionary process in a genetic algorithm.
 * The types of listeners generated include:
 * - [EvolutionPlotter]: For visualizing the evolution process through plots.
 * - [EvolutionPrinter]: For printing detailed information about each generation during evolution.
 * - [EvolutionSummary]: For summarizing the evolution process, capturing key metrics.
 *
 * By generating a diverse set of listeners, this function facilitates comprehensive testing and
 * analysis of genetic algorithms under various conditions and configurations.
 *
 * ## Example Usage:
 * ```
 * val evolutionListenerArb = Arb.evolutionListener<Int, IntGene>()
 * val evolutionListener = evolutionListenerArb.bind() // Instance of a specific EvolutionListener
 * //... setup for genetic algorithm
 * ```
 *
 * @param T The type representing the genetic data or information.
 * @param G The specific type of [Gene] associated with the genetic data.
 *
 * @return An [Arb] (Arbitrary generator) that produces instances of different [EvolutionListener] types,
 *         enabling diverse observation and analysis in genetic algorithm testing scenarios.
 */
fun <T, G> Arb.Companion.evolutionListener(): Arb<EvolutionListener<T, G>> where G : Gene<T, G> =
    choice(evolutionPlotter(), evolutionPrinter(), evolutionSummary())

/**
 * Generates an arbitrary [EvolutionPlotter] for property-based testing.
 *
 * The [EvolutionPlotter] is a type of [EvolutionListener] that tracks and records the evolution process,
 * mainly for visualization or logging purposes. This function creates instances of [EvolutionPlotter]
 * with random configurations.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the plotter will be dealing with.
 * @return An arbitrary generator producing instances of [EvolutionPlotter].
 */
fun <T, G> Arb.Companion.evolutionPlotter() where G : Gene<T, G> = arbitrary { EvolutionPlotter<T, G>() }

/**
 * Generates an arbitrary [EvolutionPrinter] for property-based testing.
 *
 * The [EvolutionPrinter] is a specialized [EvolutionListener] that prints details about the evolution
 * process at specified intervals. This function creates instances of [EvolutionPrinter] with a random
 * frequency of printing generations.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the printer will be dealing with.
 * @return An arbitrary generator producing instances of [EvolutionPrinter].
 */
fun <T, G> Arb.Companion.evolutionPrinter() where G : Gene<T, G> =
    arbitrary { EvolutionPrinter<T, G>(positiveInt().bind()) }

/**
 * Provides an arbitrary generator for creating instances of [EvolutionSummary].
 * This generator is particularly useful for property-based testing in scenarios involving genetic algorithms.
 *
 * [EvolutionSummary] is a listener that collects and summarizes the evolution process of a genetic algorithm.
 * It tracks various metrics such as fitness values, population diversity, and more, providing insights into
 * the performance and behavior of the genetic algorithm over generations.
 *
 * By generating [EvolutionSummary] instances, this function allows for the easy inclusion of evolution
 * tracking in test scenarios, enabling the assessment of genetic algorithm performance and characteristics
 * under different configurations and conditions.
 *
 * ## Example Usage:
 * ```
 * val evolutionSummaryArb = Arb.evolutionSummary<Int, IntGene>()
 * val evolutionSummary = evolutionSummaryArb.bind() // Instance of EvolutionSummary for IntGene
 * //... setup for genetic algorithm
 * ```
 *
 * @param T The type representing the genetic data or information.
 * @param G The specific type of [Gene] associated with the genetic data.
 *
 * @return An [Arb] (Arbitrary generator) that produces instances of [EvolutionSummary], facilitating the
 *         integration of evolution tracking in genetic algorithm testing scenarios.
 */
fun <T, G> Arb.Companion.evolutionSummary() where G : Gene<T, G> = arbitrary { EvolutionSummary<T, G>() }
