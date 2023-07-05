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
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.reflection.shouldBeLateInit
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import io.kotest.property.kotlinx.datetime.datetime
import io.kotest.property.kotlinx.datetime.instant
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime


class GenerationRecordTest : FreeSpec({
    "A [GenerationRecord]" - {
        "can be created with a generation number" {
            checkAll(Arb.generationRecord()) { record ->
                record.toGenerationRecord() shouldBe GenerationRecord(record.generation)
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

        "should have an initial time that" - {
            "is late initialized" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = data.toGenerationRecord()
                    record::initTime.shouldBeLateInit()
                    shouldThrowWithMessage<UninitializedPropertyAccessException>("lateinit property initTime has not been initialized") {
                        record.initTime
                    }
                }
            }

            "can be initialized" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = data.toGenerationRecord()
                    record.initTime = data.initTime
                    record.initTime shouldBe data.initTime
                }
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
    GenerationRecordData(nonNegativeInt().bind(), instant().bind())
}

/**
 * A data class that represents a generation record.
 *
 * @property generation The generation number.
 */
data class GenerationRecordData(val generation: Int, val initTime: Instant) {
    /**
     * Converts this [GenerationRecordData] object into a [GenerationRecord] object.
     *
     * This function creates a new GenerationRecord object with the same generation number
     * as this GenerationRecordData object.
     */
    fun toGenerationRecord() = GenerationRecord(generation)
}
