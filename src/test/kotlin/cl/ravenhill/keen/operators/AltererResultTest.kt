/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.altererResult
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class AltererResultTest : FreeSpec({
    "An [AltererResult]" - {
        "when created" - {
            "without explicit alterations should default to 0" {
                checkAll(Arb.population()) { population ->
                    AltererResult(population).alterations shouldBe 0
                }
            }

            "with explicit alterations should have the given value" {
                checkAll(
                    Arb.population(),
                    Arb.nonNegativeInt()
                ) { population, alterations ->
                    AltererResult(
                        population,
                        alterations
                    ).alterations shouldBe alterations
                }
            }

            "with a negative number of alterations should throw an exception" {
                checkAll(Arb.population(), Arb.negativeInt()) { population, alterations ->
                    shouldThrow<CompositeException> {
                        AltererResult(population, alterations)
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The number of alterations [$alterations] must be greater " +
                                "than or equal to 0"
                    )
                }
            }
        }

        "can be destructured" {
            checkAll(Arb.altererResult()) { result ->
                val (population, alterations) = result
                population shouldBe result.population
                alterations shouldBe result.alterations
            }
        }

        "can be converted to a string" {
            checkAll(Arb.altererResult()) { result ->
                // @formatter:off
                result.toString() shouldBe "AltererResult(" +
                    "population=${result.population.map { it.toSimpleString() }}, " +
                    "alterations=${result.alterations})"
            }
        }

        "equality should" - {
            "be reflexive" {
                checkAll(Arb.altererResult()) { result ->
                    result shouldBe result
                }
            }

            "be symmetric" {
                checkAll(
                    Arb.population(),
                    Arb.nonNegativeInt()
                ) { population, alterations ->
                    val result1 = AltererResult(population, alterations)
                    val result2 = AltererResult(population, alterations)
                    result1 shouldBe result2
                    result2 shouldBe result1
                }
            }

            "be transitive" {
                checkAll(
                    Arb.population(),
                    Arb.nonNegativeInt()
                ) { population, alterations ->
                    val result1 = AltererResult(population, alterations)
                    val result2 = AltererResult(population, alterations)
                    val result3 = AltererResult(population, alterations)
                    result1 shouldBe result2
                    result2 shouldBe result3
                    result1 shouldBe result3
                }
            }
        }

        "hashing should" - {
            "be consistent with equality" {
                checkAll(
                    Arb.population(),
                    Arb.nonNegativeInt()
                ) { population, alterations ->
                    val result1 = AltererResult(population, alterations)
                    val result2 = AltererResult(population, alterations)
                    result1 shouldHaveSameHashCodeAs result2
                }
            }

            "be different for different results" {
                checkAll(Arb.altererResult(), Arb.altererResult()) { result1, result2 ->
                    assume { result1 shouldNotBe result2 }
                    result1 shouldNotHaveSameHashCodeAs result2
                }
            }
        }
    }
})
