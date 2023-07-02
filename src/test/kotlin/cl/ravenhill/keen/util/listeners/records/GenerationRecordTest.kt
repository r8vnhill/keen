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
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll


class GenerationRecordTest : FreeSpec({
    "A [GenerationRecord] " - {
        "can be created with a generation number" {
            checkAll(Arb.generationRecord()) { generation ->
                generation.toGenerationRecord() shouldBe GenerationRecord(generation.generation)
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

/**
 * Provides an arbitrary (Arb) instance that generates a [GenerationRecordData] object.
 *
 * The provided Arb object generates a GenerationRecordData object, which contains a non-negative
 * integer representing the generation number. This is useful when you need an arbitrary
 * generation record for testing or other purposes.
 *
 * @return An Arb instance that generates a [GenerationRecordData] object.
 */
fun Arb.Companion.generationRecord() = arbitrary {
    GenerationRecordData(nonNegativeInt().bind())
}

/**
 * A data class that represents a generation record.
 *
 * @property generation The generation number.
 */
data class GenerationRecordData(val generation: Int) {
    /**
     * Converts this [GenerationRecordData] object into a [GenerationRecord] object.
     *
     * This function creates a new GenerationRecord object with the same generation number
     * as this GenerationRecordData object.
     */
    fun toGenerationRecord() = GenerationRecord(generation)
}
