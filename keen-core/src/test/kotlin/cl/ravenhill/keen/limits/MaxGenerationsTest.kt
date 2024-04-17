/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.arbRanker
import cl.ravenhill.keen.arb.evolution.arbEvolutionState
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.limits.arbMaxGenerations
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class MaxGenerationsTest : FreeSpec({

    "A MaxGenerations limit" - {
        "should have a generations number that" - {
            "stores the value passed to the constructor when the value is positive" {
                checkAll(Arb.positiveInt()) { generations ->
                    MaxGenerations<Nothing, NothingGene>(generations).generations shouldBe generations
                }
            }

            "throws an exception when the value is non-positive" {
                checkAll(Arb.nonPositiveInt()) { generations ->
                    shouldThrow<CompositeException> {
                        MaxGenerations<Nothing, NothingGene>(generations)
                    }.shouldHaveInfringement<IntConstraintException>("The number of generations must be positive")
                }
            }
        }

        "should have an engine property that" - {
            "is null when the limit is created" {
                checkAll(arbMaxGenerations<Nothing, NothingGene>()) { limit ->
                    limit.engine.shouldBeNull()
                }
            }
        }

        "when invoking" - {
            "should return true when the current generation is less than or equal to the limit" {
                checkAll(arbIntEvolutionState().flatMap { state ->
                    arbMaxGenerations<Int, IntGene>(Arb.positiveInt(state.generation))  // limit <= state.generation
                        .map { state to it }
                }) { (state, limit) ->
                    limit(state).shouldBeTrue()
                }
            }

            "should return false when the current generation is greater than the limit" {
                checkAll(arbIntEvolutionState().flatMap { state ->
                    arbMaxGenerations<Int, IntGene>(
                        Arb.int(state.generation + 1..Int.MAX_VALUE)    // limit > state.generation
                    ).map { state to it }
                }) { (state, limit) ->
                    limit(state).shouldBeFalse()
                }
            }
        }
    }
})

fun arbIntGenotype() = arbGenotype(arbIntChromosome())
fun arbIntIndividual() = arbIndividual(arbIntGenotype())
fun arbIntPopulation() = arbPopulation(arbIntIndividual())
fun arbIntEvolutionState() = arbEvolutionState(arbIntPopulation(), arbRanker(), Arb.int(2..1000))
