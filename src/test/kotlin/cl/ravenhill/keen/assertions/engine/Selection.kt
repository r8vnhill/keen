/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.engine

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.util.ceil
import cl.ravenhill.keen.util.floor
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

/**
 * Tests the functionality of the engine's offspring selection process within the Keen framework.
 * These tests ensure that the offspring selection is conducted correctly, producing populations
 * of expected size and composition based on the engine's configuration.
 *
 * Two key aspects are tested:
 * - The size of the resulting population after offspring selection.
 * - The composition of the resulting population, ensuring it aligns with the expected individuals.
 *
 * Property-based testing is utilized to cover a range of scenarios and configurations.
 */
suspend fun FreeSpecContainerScope.`check Engine offspring selection`() {
    "when selecting offspring" - {
        "with a non-empty population" - {
            "should return a new population with the expected size" {
                checkAll(
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                        Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                    }
                ) { (engine, state) ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val evaluated = engine.evaluate(state)
                    val result = engine.selectOffspring(evaluated)
                    result.population shouldHaveSize ((1 - engine.survivalRate) * engine.populationSize).floor()
                }
            }

            "should return a new population with the expected individuals" {
                checkAll(
                    Arb.long() compose {
                        Core.random = Random(it)
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                    },
                    Arb.evolutionState(Arb.population(size = 1..10))
                ) { (seed, engine), state ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val result = engine.selectOffspring(state)
                    Core.random = Random(seed)
                    val expected = EvolutionState(
                        state.generation, engine.offspringSelector.select(
                            state.population,
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

/**
 * Tests the survivor selection functionality of the [EvolutionEngine] in a genetic algorithm.
 *
 * This test suite evaluates the ability of the [EvolutionEngine] to correctly select survivors
 * from a given population. The tests are structured to assess the effectiveness of survivor
 * selection under various conditions and configurations.
 *
 * ## Test Scenarios:
 * - **Non-empty Population Selection**: Verifies that the engine can select survivors from a
 *   non-empty population and that the resulting population size matches the expected size based
 *   on the engine's configuration (i.e., survival rate and population size).
 * - **Individual Selection Accuracy**: Checks if the selected individuals in the resulting population
 *   are those expected from the survivor selection process, ensuring that the engine's survivor selector
 *   is functioning as intended.
 *
 * The tests use property-based testing provided by Kotest's `Arbitrary` to generate random configurations
 * for engines, genotypes, and evolution states. This approach allows for a comprehensive evaluation
 * of the survivor selection mechanism across a wide range of scenarios.
 * ```
 */
suspend fun FreeSpecContainerScope.`check Engine survivor selection`() {
    "when selecting survivors" - {
        "with a non-empty population" - {
            "should return a new population with the expected size" {
                checkAll(
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                        Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                    }
                ) { (engine, state) ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val evaluated = engine.evaluate(state)
                    val result = engine.selectSurvivors(evaluated)
                    result.population shouldHaveSize (engine.survivalRate * engine.populationSize).ceil()
                }
            }

            "should return a new population with the expected individuals" {
                checkAll(
                    Arb.long() compose {
                        Core.random = Random(it)
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                    },
                    Arb.evolutionState(Arb.population(size = 1..10))
                ) { (seed, engine), state ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val result = engine.selectSurvivors(state)
                    Core.random = Random(seed)
                    val expected = EvolutionState(
                        state.generation, engine.survivorSelector.select(
                            state.population,
                            (engine.survivalRate * engine.populationSize).ceil(),
                            engine.optimizer
                        )
                    )
                    result shouldBe expected
                }
            }
        }
    }
}
