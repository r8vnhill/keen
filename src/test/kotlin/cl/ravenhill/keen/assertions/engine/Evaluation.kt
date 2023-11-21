/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.engine

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.assume
import io.kotest.property.checkAll

/**
 * This test suite is designed to assess the functionality of the engine's evaluation process
 * within the Keen framework, focusing on various scenarios where a population of individuals
 * may or may not have been previously evaluated.
 *
 * The suite consists of three primary test cases:
 *
 * 1. **Fully Evaluated Population**: This test verifies that if all individuals in a population
 *    are already evaluated, the engine's evaluation process should maintain the population
 *    unchanged.
 *
 * 2. **Partially Evaluated Population**: This test checks that if some individuals in the
 *    population are not evaluated, the engine's evaluation process should ensure that all
 *    individuals are evaluated in the resultant population.
 *
 * 3. **Population Size Mismatch**: This test ensures that the engine throws an exception if the
 *    size of the population being evaluated does not match the engine's expected population size.
 *
 * Each test case utilizes property-based testing to cover a wide range of scenarios. The test suite
 * leverages Kotest's property-based testing capabilities to automatically generate test data.
 *
 * @OptIn This function uses experimental features from the Kotest framework, as indicated by the
 *        `ExperimentalKotest` annotation.
 */
@OptIn(ExperimentalKotest::class)
suspend fun FreeSpecContainerScope.`check Engine evaluation`() {
    "should be able to evaluate a population" - {
        "when the population has un-evaluated individuals" - {
            "should return the same population if all individuals are evaluated" {
                checkAll(
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                        Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                    }
                ) { (engine, state) ->
                    assume {
                        state.population.all { it.isEvaluated() }.shouldBeTrue()
                    }
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val result = engine.evaluate(state)
                    result.population.all { it.isEvaluated() }.shouldBeTrue()
                    result.population shouldHaveSize engine.populationSize
                }
            }

            "should return a new population with all individuals evaluated" {
                checkAll(
                    PropTestConfig(iterations = 50),
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                        Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                    }
                ) { (engine, state) ->
                    assume {
                        state.population.any { it.isNotEvaluated() }.shouldBeTrue()
                    }
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val result = engine.evaluate(state)
                    result.population.all { it.isEvaluated() }.shouldBeTrue()
                    result.population shouldHaveSize engine.populationSize
                }
            }

            "should throw an exception if the population size is not the expected by the engine" {
                checkAll(
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()),
                    Arb.evolutionState(Arb.population())
                ) { engine, state ->
                    assume {
                        state.population shouldNotHaveSize engine.populationSize
                    }
                    shouldThrow<CompositeException> {
                        engine.evaluate(state)
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "Population size must be the same as the expected population size"
                    )
                }
            }
        }
    }
}