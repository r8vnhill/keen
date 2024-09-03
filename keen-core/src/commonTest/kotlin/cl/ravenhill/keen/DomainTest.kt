/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.matchers.shouldContainExceptionOfType
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.checkAll

class DomainTest : FreeSpec({
    "The Domain" - {
        "should have an equality threshold that" - {
            "starts at the DEFAULT_EQUALITY_THRESHOLD" {
                Domain.equalityThreshold shouldBe Domain.DEFAULT_EQUALITY_THRESHOLD
            }

            "can be set to a positive value" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.double().filter { it > 0 }) { threshold ->
                    Domain.equalityThreshold = threshold
                    Domain.equalityThreshold shouldBe threshold
                }
            }

            "can be set to 0" { // We test this to avoid tricky case of -0.0 in PBT
                Domain.equalityThreshold = 0.0
                Domain.equalityThreshold shouldBe 0.0
            }

            "throws an exception if set to a negative value" {
                checkAll(Arb.double().filter { it < 0 }) { threshold ->
                    shouldThrowUnit<CompositeException> {
                        Domain.equalityThreshold = threshold
                    }.shouldContainExceptionOfType<DoubleConstraintException>(
                        "The equality threshold ($threshold) must be at least 0.0"
                    )
                }
            }

            "throws an exception if set to NaN" {
                shouldThrowUnit<CompositeException> {
                    Domain.equalityThreshold = Double.NaN
                }.shouldContainExceptionOfType<DoubleConstraintException>(
                    "The equality threshold (NaN) must be a number"
                )
            }
        }
    }
})
