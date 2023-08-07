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
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.reflection.shouldBeLateInit
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource


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

        "should have an ``initial time`` that" - {
            "is late initialized" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = data.toGenerationRecord()
                    record::startTime.shouldBeLateInit()
                    shouldThrowWithMessage<UninitializedPropertyAccessException>("lateinit property startTime has not been initialized") {
                        record.startTime
                    }
                }
            }

            "can be initialized" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = data.toGenerationRecord()
                    record.startTime = data.initTime
                    record.startTime shouldBe data.initTime
                }
            }
        }

        "should have a steady generation number that" - {
            "is initialized to 0" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = data.toGenerationRecord()
                    record.steady shouldBe 0
                }
            }

            "can be set to a non-negative integer" {
                checkAll(Arb.generationRecord(), Arb.nonNegativeInt()) { data, steady ->
                    val record = data.toGenerationRecord()
                    record.steady shouldBe 0
                    record.steady = steady
                    record.steady shouldBe steady
                }
            }

            "should throw an exception if the generation number is negative" {
                checkAll(Arb.generationRecord(), Arb.negativeInt()) { data, steady ->
                    val record = data.toGenerationRecord()
                    shouldThrowUnit<EnforcementException> {
                        record.steady = steady
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("The generation number [$steady] must be positive")
                    )
                }
            }
        }

        "should have a [PopulationRecord] that" - {
            "have a resulting population that" - {
                "is initialized as an empty list" {
                    checkAll(Arb.generationRecord()) { data ->
                        val record = data.toGenerationRecord()
                        record.population.resulting.shouldBeEmpty()
                    }
                }
                // TODO: Implement when Arb.population is implemented.
//                "can be initialized" {
//                    checkAll(Arb.generationRecord(), Arb.populationRecord()) { data, population ->
//                        val record = data.toGenerationRecord()
//                        record.population = population
//                        record.population shouldBe population
//                    }
//                }
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
    GenerationRecordData(nonNegativeInt().bind(), TimeSource.Monotonic.markNow())
}

/**
 * A data class that represents a generation record.
 *
 * @property generation The generation number.
 */
data class GenerationRecordData(val generation: Int, val initTime: TimeMark) {
    /**
     * Converts this [GenerationRecordData] object into a [GenerationRecord] object.
     *
     * This function creates a new GenerationRecord object with the same generation number
     * as this GenerationRecordData object.
     */
    fun toGenerationRecord() = GenerationRecord(generation)
}
