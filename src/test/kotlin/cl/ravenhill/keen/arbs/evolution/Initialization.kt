/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import kotlin.random.Random
import io.kotest.property.checkAll

/**
 * Tests the process of starting an evolutionary engine in the Keen framework.
 *
 * This test suite evaluates the behavior of the evolutionary engine when initiating the evolution
 * process from different states. It verifies the engine's ability to correctly handle both empty
 * and pre-populated states, ensuring the evolution starts as expected.
 *
 * ## Test Scenarios:
 * - **Evolution Start from an Empty State**: Validates that when evolution starts with an empty state,
 *   the engine creates a new population of the expected size. This scenario is crucial for ensuring
 *   the engine can initiate evolution from a state with no pre-existing individuals.
 * - **Evolution Start from a Given Non-Empty State**: Ensures that when provided with a pre-populated
 *   state, the engine starts the evolution without altering the initial state. This test is important
 *   for scenarios where evolution continues from a specific generation or population state.
 *
 * ## Testing Approach:
 * The tests utilize Kotest's property-based testing features, generating various engine configurations
 * and initial states to cover a wide range of scenarios. This approach helps verify the robustness of the
 * engine's evolution start process under different conditions.
 *
 * ### Example Usage:
 *
 * ```kotlin
 * class EvolutionEngineTest : FreeSpec({
 *     include(`test Engine evolution start`())
 *     //... other tests
 * })
 * ```
 *
 * In the example above, the test suite includes the tests defined in this file, which are executed
 * as part of the test suite.
 */
fun `test Engine evolution start`() = freeSpec {
    "Evolution start process" - {
        "from an empty state" - {
            "should create a new population with expected size" {
                checkAll(
                    Arb.long().map {
                        Core.random = Random(it) // Ensure tests are reproducible
                        Arb.evolutionState<Int, IntGene>(Arb.constant(emptyList())).next()
                    },
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { state, engine ->
                    with(engine.startEvolution(state)) {
                        generation shouldBe state.generation
                        population.size shouldBe engine.populationSize
                    }
                    Core.random = Random // Reset random seed
                }
            }
        }

        "from a given non-empty state" - {
            "should return the same state without changes" {
                checkAll(
                    Arb.evolutionState(Arb.population(size = 1..10)),
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { state, engine ->
                    val result = engine.startEvolution(state)
                    result shouldBe state // Check if state is unchanged
                }
            }
        }
    }
}
