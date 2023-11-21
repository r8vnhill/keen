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
import cl.ravenhill.keen.arbs.operators.mutator
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

suspend fun FreeSpecContainerScope.`check Engine alteration`() {
    "when altering a population" - {
        "with a non-empty population" - {
            "should return a new population with the expected size" {
                checkAll(
                    Arb.engine(Arb.intGenotypeFactory(), Arb.mutator<Int, IntGene>()) compose {
                        Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
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
                    Arb.long() compose {
                        Core.random = Random(it)
                        Arb.engine(Arb.intGenotypeFactory(), Arb.mutator<Int, IntGene>())
                    },
                    Arb.evolutionState(Arb.population(size = 1..10))
                ) { (seed, engine), state ->
                    engine.listeners.forEach { it.onGenerationStarted(state.population) }
                    val result = engine.alter(state)
                    Core.random = Random(seed)
                    val expected = engine.alterer(state.population, engine.populationSize)
                    result shouldBe expected
                }
            }
        }
    }
}