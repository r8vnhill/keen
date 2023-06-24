/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll


class GenerationRecordTest : FreeSpec({
    "A [GenerationRecord] " - {
        "can be created with a generation number" {
            checkAll(Arb.nonNegativeInt()) { generation ->
                GenerationRecord(generation) shouldBe GenerationRecord(generation)
            }
        }

        "should throw an exception if the generation number is negative" {
            checkAll(Arb.negativeInt()) { generation ->
                shouldThrow<EnforcementException> {
                    GenerationRecord(generation)
                }.shouldHaveInfringement<IntRequirementException>(
                    unfulfilledConstraint("The generation number [$generation] must be positive")
                )
            }
        }
    }
})