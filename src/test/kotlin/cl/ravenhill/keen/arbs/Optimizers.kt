package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element

/**
 * Generates an arbitrary [IndividualOptimizer] for property-based testing.
 *
 * This function creates instances of optimizers used in genetic algorithms, specifically [FitnessMinimizer]
 * and [FitnessMaximizer]. These optimizers are fundamental in genetic algorithms for comparing and selecting
 * individuals based on fitness. The function uses Kotest's [Arb] (Arbitrary) API to randomly choose between
 * a fitness minimizer and maximizer, catering to different optimization strategies (minimizing or maximizing
 * fitness).
 *
 * ## Usage:
 * - To generate an optimizer that could be either a fitness minimizer or maximizer:
 *   ```kotlin
 *   val optimizerArb = Arb.optimizer<Int, IntGene>()
 *   val optimizer = optimizerArb.bind() // Instance of either FitnessMinimizer or FitnessMaximizer
 *   ```
 *
 * This function is especially useful for testing genetic algorithms where different optimization strategies
 * might be employed, and there's a need to test the algorithm's behavior under these varying conditions.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] that the optimizer will work with.
 *
 * @return An [Arb] that generates instances of [IndividualOptimizer], either a [FitnessMinimizer] or
 *         [FitnessMaximizer], to be used in genetic algorithms.
 */
fun <T, G> Arb.Companion.optimizer(): Arb<IndividualOptimizer<T, G>> where G : Gene<T, G> =
    element(FitnessMinimizer(), FitnessMaximizer())
