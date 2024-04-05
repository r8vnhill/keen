/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.limits.arbGenerationLimit
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.checkAll

class GenerationLimitTest : FreeSpec({

    "A Generation Limit" - {
        "should have a generations number that" - {
            "stores the value passed to the constructor when the value is positive" {
                checkAll(Arb.int()) { generations ->
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
                checkAll(arbGenerationLimit<Nothing, NothingGene>()) { limit ->
                    limit.engine shouldBe null
                }
            }

//            "can be set to a non-null value" {
//                checkAll(Arb.generationLimit<Nothing, NothingGene>(), Arb.int()) { limit, engine ->
//                    limit.engine = engine
//                    limit.engine shouldBe engine
//                }
//            }
        }
    }
})
