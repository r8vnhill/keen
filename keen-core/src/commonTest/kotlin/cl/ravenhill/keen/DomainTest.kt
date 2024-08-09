/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.matchers.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class DomainTest : FreeSpec({

    afterEach {
        Domain.random = Random.Default
    }

    "The Domain" - {
        "should have an equality threshold that" - {
            "starts at 0.0001" {
                Domain.equalityThreshold shouldBe 0.0001
            }

            "can be set to a positive value" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.double().filter { it > 0 }
                ) { threshold ->
                    Domain.equalityThreshold = threshold
                    Domain.equalityThreshold shouldBe threshold
                }
            }

            "can be set to 0" {
                Domain.equalityThreshold = 0.0
                Domain.equalityThreshold shouldBe 0.0
            }

            "should throw an exception if set to a value that's not greater than or equal to zero" {
                checkAll(Arb.double().filterNot { it >= 0 }) { threshold ->
                    shouldThrowUnit<CompositeException> {
                        Domain.equalityThreshold = threshold
                    }.shouldHaveInfringement<DoubleConstraintException>(
                        "The equality threshold ($threshold) must be greater than or equal to zero"
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
