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
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.util.floor
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.random.Random
import kotlin.reflect.KProperty1

/**
 * A test suite for evaluating the selection process within an [EvolutionEngine].
 * This function is designed to test both offspring and survivor selection mechanisms in the engine.
 *
 * The function uses a property reference to dynamically test either the offspring or survivor
 * selection process based on the provided selector.
 *
 * ## Test Scenarios:
 * - **Population Size Check**: Verifies that the resulting population size after selection matches the
 *   expected size based on the engine's configuration.
 * - **Population Content Check**: Ensures that the individuals in the resulting population are those
 *   expected from the selection process.
 *
 * ## Usage:
 * This function is called with specific parameters to test either the offspring or survivor selection
 * process within the engine.
 *
 * @param propertyName A [String] representing the name of the property (either "offspring" or "survivor")
 *   to be tested. This name is used in test descriptions for clarity.
 * @param select A lambda function that defines the selection process to test. It takes an [EvolutionEngine]
 *   and an [EvolutionState] and returns a new [EvolutionState] after applying the selection process.
 * @param selector A [KProperty1] property reference pointing to the selector (either offspringSelector or
 *   survivorSelector) in the [EvolutionEngine].
 */
private fun `test Engine selection`(
    propertyName: String,
    select: EvolutionEngine<Int, IntGene>.(EvolutionState<Int, IntGene>) -> EvolutionState<Int, IntGene>,
    selector: KProperty1<EvolutionEngine<Int, IntGene>, Selector<Int, IntGene>>
) = freeSpec {
    "An Evolution Engine" - {
        "when selecting $propertyName" - {
            "with a non-empty population" - {
                "should return a new population with the expected size" {
                    checkAll(
                        Arb.evolutionEngine(Arb.intGenotypeFactory(), Arb.intAlterer()).map {
                            it to Arb.evolutionState(Arb.population(size = it.populationSize..it.populationSize)).next()
                        }) { (engine, state) ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val evaluated = engine.evaluate(state)
                        val result = engine.select(evaluated)
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
                            selector.get(engine).select(
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

/**
 * Test suite for evaluating the offspring selection process within an [EvolutionEngine].
 *
 * This function leverages the `test Engine selection` to specifically test the offspring selection
 * mechanism of the engine. It verifies that the engine correctly selects offspring based on its
 * configuration and the evolutionary state.
 *
 * ## Functionality:
 * - **Population Size Check**: Ensures the size of the population after offspring selection matches
 *   the expected size, based on the engine's survival rate and population size.
 * - **Population Content Check**: Confirms that the individuals in the resulting population align
 *   with those expected from the offspring selection process.
 *
 * ## Usage:
 * This test suite is included in a larger test framework to validate the behavior of the offspring
 * selector in various scenarios.
 *
 * ```kotlin
 * class EvolutionEngineTest : FreeSpec({
 *     include(`test Engine offspring selection`())
 *     //... other tests
 * })
 * ```
 */
fun `test Engine offspring selection`() = `test Engine selection`(
    "offspring",
    EvolutionEngine<Int, IntGene>::selectOffspring,
    EvolutionEngine<Int, IntGene>::offspringSelector
)

/**
 * Test suite for evaluating the survivor selection process within an [EvolutionEngine].
 *
 * Utilizes the `test Engine selection` function to specifically assess the survivor selection
 * mechanism. This suite ensures the engine's ability to select survivors appropriately from the
 * given population.
 *
 * ## Functionality:
 * - **Population Size Check**: Confirms that the size of the population post-survivor selection
 *   aligns with expectations based on the engine's survival rate and population size.
 * - **Population Content Check**: Verifies that the selected survivors are those anticipated from
 *   the survivor selection process, as defined by the engine's configuration.
 *
 * ## Usage:
 * Incorporated into a comprehensive test suite to ensure the correct functionality of the survivor
 * selector across different engine configurations.
 *
 * ```kotlin
 * class EvolutionEngineTest : FreeSpec({
 *     include(`test Engine survivor selection`())
 *     //... other tests
 * })
 * ```
 */
fun `test Engine survivor selection`() = `test Engine selection`(
    "survivor",
    EvolutionEngine<Int, IntGene>::selectSurvivors,
    EvolutionEngine<Int, IntGene>::survivorSelector
)
