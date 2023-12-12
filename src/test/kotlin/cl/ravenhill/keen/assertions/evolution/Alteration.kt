/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.evolution

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.evolution.evolutionEngine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.operators.mutator
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.random.Random

/**
 * Tests the alteration process in an [EvolutionEngine] within the Keen framework.
 *
 * This test suite assesses the engine's ability to correctly perform alterations on a population
 * during the evolutionary process. Alterations typically include mutations or crossovers that
 * introduce genetic diversity into the population.
 *
 * ## Test Scenarios:
 * - **Altering a Non-Empty Population**: Verifies that the engine can effectively alter a
 *   non-empty population, ensuring that the size and composition of the population post-alteration
 *   meet the expected criteria.
 *
 * ### Key Checks:
 * - **Population Size Consistency**: Confirms that the size of the population remains consistent
 *   after alteration, matching the engine's predefined population size.
 * - **Population Content Accuracy**: Assesses whether the individuals in the altered population
 *   are as expected, based on the engine's configured alterer.
 *
 * ## Testing Approach:
 * The tests use property-based testing provided by Kotest's `Arbitrary` to generate various engine
 * configurations and initial states. This approach facilitates a comprehensive evaluation of the
 * alteration mechanism across diverse scenarios and configurations.
 *
 * ### Example Usage in Testing:
 *
 * ```kotlin
 * class EvolutionEngineTest : FreeSpec({
 *     include(`test Engine alteration`())
 *     //... additional tests
 * })
 * ```
 *
 * The example above demonstrates how to include this test suite in a broader testing framework,
 * ensuring the engine's alteration process is robust and functions correctly under various conditions.
 */
fun `test Engine alteration`() = freeSpec {
    "An Evolution Engine when altering a population" - {
        "that's not empty" - {
            "should return a new population with the expected size" {
                checkAll(
                    Arb.evolutionEngine(Arb.intGenotypeFactory(), Arb.mutator<Int, IntGene>()).map {
                        it to Arb.evolutionState(Arb.population(size = it.populationSize..it.populationSize)).next()
                    }
                ) { (engine, state) ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val evaluated = engine.evaluate(state)
                    val result = engine.alter(evaluated)
                    result.population shouldHaveSize engine.populationSize
                }
            }

            "should return a new population with the expected individuals" {
                checkAll(
                    Arb.long().map {
                        Core.random = Random(it)
                        it to Arb.evolutionEngine(Arb.intGenotypeFactory(), Arb.mutator<Int, IntGene>()).next()
                    }.map { (seed, engine) ->
                        Triple(
                            seed,
                            engine,
                            Arb.evolutionState(Arb.population(size = engine.populationSize..engine.populationSize))
                                .next()
                        )
                    }) { (seed, engine, state) ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val evaluated = engine.evaluate(state)
                    val result = engine.alter(evaluated)
                    Core.random = Random(seed)
                    val expected = engine.alterer(evaluated.population, engine.populationSize)
                    result shouldBe expected
                }
            }
        }
    }
}
