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
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

suspend fun FreeSpecContainerScope.`check Engine evolution start`() {
    "evolution start process" - {
        "from an empty state" - {
            "should create a new population with expected size" {
                checkAll(
                    Arb.long() compose {
                        Core.random = Random(it)
                        Arb.evolutionState<Int, IntGene>(Arb.constant(emptyList()))
                    },
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { (seed, state), engine ->
                    with(engine.startEvolution(state)) {
                        generation shouldBe state.generation
                        population.size shouldBe engine.populationSize
                    }
                    Core.random = Random
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