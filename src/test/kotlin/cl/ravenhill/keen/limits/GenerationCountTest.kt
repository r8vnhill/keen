/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.limits.generationCount
import cl.ravenhill.keen.assertions.util.listeners.`test ListenLimit with varying generations`
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class GenerationCountTest : FreeSpec({

    "A [GenerationCount]" - {
        "when created with a positive integer" - {
            "correctly initializes its count value" {
                checkAll(Arb.positiveInt()) {
                    GenerationCount<Int, IntGene>(it).count shouldBe it
                }
            }
        }

        "when attempted with a non-positive integer" - {
            "throws an appropriate constraint violation exception" {
                checkAll(Arb.nonPositiveInt()) {
                    shouldThrow<CompositeException> {
                        GenerationCount<Int, IntGene>(it)
                    }.shouldHaveInfringement<IntConstraintException>("Generation count [$it] must be at least 1")
                }
            }
        }

        "when invoked to check the limit condition" - {
            "accurately evaluates whether the generation count exceeds the limit" {
                `test ListenLimit with varying generations`({ Arb.generationCount<Int, IntGene>(it) }) { count ->
                    generation >= count
                }
            }
        }
    }
})
