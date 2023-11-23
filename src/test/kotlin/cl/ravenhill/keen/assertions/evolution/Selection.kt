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
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.util.floor
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.random.Random

/**
 * Tests the offspring selection process of the [EvolutionEngine] in genetic algorithms.
 *
 * This test suite evaluates the engine's ability to correctly select offspring from a given population
 * based on the engine's configuration. It focuses on two primary aspects: the size and the composition
 * of the population after the offspring selection process.
 *
 * ## Test Structure:
 * - **"An Evolution Engine"**: The top-level description of the test suite.
 *   - **"when selecting offspring"**: Specifies that the tests are focusing on the offspring selection phase of the
 *     engine.
 *     - **"with a non-empty population"**: Indicates that the tests are conducted with populations that are not empty.
 *       - **"should return a new population with the expected size"**: Tests whether the size of the population after
 *         offspring selection matches the expected size, calculated as the floor value of the product of (1 -
 *         survivalRate)
 *         and the engine's population size.
 *       - **"should return a new population with the expected individuals"**: Tests if the individuals in the
 *         population after offspring selection match those expected based on the engine's offspring selector
 *         configuration.
 *
 * ## Testing Approach:
 * The tests use Kotest's property-based testing features, generating various configurations of engines and initial states
 * to comprehensively evaluate the offspring selection mechanism. This approach ensures robust testing across a wide range
 * of scenarios.
 *
 * ### Example Usage:
 * ```kotlin
 * class EvolutionEngineTest : FreeSpec({
 *     include(`test Engine offspring selection`())
 *     //... other tests
 * })
 * ```
 *
 * The tests are included as part of a larger test suite for the evolutionary engine, ensuring that the offspring
 * selection functionality is thoroughly validated as part of the engine's overall behavior.
 */
fun `test Engine offspring selection`() = freeSpec {
    "An Evolution Engine" - {
        "when selecting offspring" - {
            "with a non-empty population" - {
                "should return a new population with the expected size" {
                    checkAll(
                        Arb.evolutionEngine(Arb.intGenotypeFactory(), Arb.intAlterer()).map {
                            it to Arb.evolutionState(Arb.population(size = it.populationSize..it.populationSize)).next()
                        }) { (engine, state) ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val evaluated = engine.evaluate(state)
                        val result = engine.selectOffspring(evaluated)
                        result.population.size shouldBe ((1 - engine.survivalRate) * engine.populationSize).floor()
                    }
                }

                "should return a new population with the expected individuals" {
                    checkAll(
                        Arb.long().map {
                            Core.random = Random(it)
                            it to Arb.evolutionEngine(Arb.intGenotypeFactory(), Arb.intAlterer()).next()
                        }.map { (seed, engine) ->
                            Triple(
                                seed,
                                engine,
                                Arb.evolutionState(Arb.population(size = engine.populationSize..engine.populationSize))
                                    .next()
                            )
                        }
                    ) { (seed, engine, state) ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val evaluated = engine.evaluate(state)
                        val result = engine.selectOffspring(evaluated)
                        Core.random = Random(seed)
                        val expected = EvolutionState(
                            state.generation,
                            engine.offspringSelector.select(
                                evaluated.population,
                                ((1 - engine.survivalRate) * engine.populationSize).floor(),
                                engine.optimizer
                            )
                        )
                        result shouldBe expected
                    }
                }
            }
        }
    }
}
