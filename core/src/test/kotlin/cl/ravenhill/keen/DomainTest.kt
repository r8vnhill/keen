/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.shouldHaveInfringement
import cl.ravenhill.keen.prog.Program
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKeen::class)
class DomainTest : FreeSpec({

    afterEach {
        Domain.random = Random.Default
        Domain.maxProgramDepth = Program.DEFAULT_MAX_DEPTH
    }

    "The Domain" - {
        "should have an equality threshold that" - {
            "starts at 0.0001" {
                Domain.equalityThreshold shouldBe 0.0001
            }

            "can be changed" {
                checkAll(Arb.double().filterNot { it.isNaN() }) { threshold ->
                    Domain.equalityThreshold = threshold
                    Domain.equalityThreshold shouldBe threshold
                }
            }
        }

        "should have maximum program depth property that" - {
            "defaults to 7" {
                Domain.maxProgramDepth shouldBe 7
            }

            "can be set to a positive value" {
                checkAll(Arb.positiveInt()) { depth ->
                    Domain.maxProgramDepth = depth
                    Domain.maxProgramDepth shouldBe depth
                }
            }

            "cannot be set to a non-positive value" {
                checkAll(Arb.nonPositiveInt()) { depth ->
                    shouldThrowUnit<CompositeException> {
                        Domain.maxProgramDepth = depth
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The maximum program depth [$depth] must be positive"
                    )
                }
            }
        }

        "should have a random number generator that" - {
            "defaults to Random.Default" {
                Domain.random shouldBe Random.Default
            }

            "can be changed" {
                checkAll(Arb.random()) { random ->
                    Domain.random = random
                    Domain.random shouldBe random
                }
            }
        }
    }
})
