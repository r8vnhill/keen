/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class EvolutionStateTest : FreeSpec({

    "An [EvolutionState]" - {
        "when created" - {
            "can be created with a population and generation" {
                checkAll(Arb.population(), Arb.nonNegativeInt()) { population, generation ->
                    val state = EvolutionState(generation, population)
                    state.population shouldBe population
                    state.generation shouldBe generation
                }
            }

            "should throw an exception when the generation is negative" {
                checkAll(Arb.population(), Arb.negativeInt()) { population, generation ->
                    shouldThrow<CompositeException> {
                        EvolutionState(generation, population)
                    }.shouldHaveInfringement<IntConstraintException>("Generation [$generation] must be non-negative")
                }
            }
        }

        "can be created as empty" {
            val state = EvolutionState.empty<Int, IntGene>()
            state.population.shouldBeEmpty()
            state.generation shouldBe 0
        }

        "can advance to the next state" {
            checkAll(Arb.population(), Arb.int(0..<Int.MAX_VALUE)) { population, generation ->
                val state = EvolutionState(generation, population)
                val nextState = state.next()
                nextState.population shouldBe population
                nextState.generation shouldBe generation + 1
            }
        }
    }
})
