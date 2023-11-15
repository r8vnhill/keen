/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.limits.generationCount
import cl.ravenhill.keen.arbs.limits.steadyGenerations
import cl.ravenhill.keen.assertions.util.listeners.`test ListenLimit with varying generations`
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class SteadyGenerationsTest : FreeSpec({

    "A [SteadyGenerations] limit" - {
        "can be created with a positive integer" {
            checkAll(Arb.positiveInt()) { numGenerations ->
                SteadyGenerations<Nothing, NothingGene>(numGenerations).generations shouldBe numGenerations
            }
        }

        "cannot be created with a non-positive integer" {
            checkAll(Arb.nonPositiveInt()) { numGenerations ->
                shouldThrow<CompositeException> {
                    SteadyGenerations<Nothing, NothingGene>(numGenerations)
                }.shouldHaveInfringement<IntConstraintException>(
                    "Number of steady generations [$numGenerations] must be a positive integer"
                )
            }
        }

        "when invoked to check the limit condition" - {
            "accurately evaluates whether the generation count exceeds the limit" {
                `test ListenLimit with varying generations`({ Arb.steadyGenerations<Int, IntGene>(it) }) { count ->
                    evolution.generations.last().steady >= count
                }
            }
        }
    }
})
