/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
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
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.time.TimeSource


/**
 * Test class for validating functionalities of [GenerationRecord].
 *
 * `GenerationRecord` represents a record of a particular generation in an evolutionary algorithm,
 * storing essential data about that generation such as its number, start time, steady state and the
 * associated population.
 * This test ensures that the basic functionalities of the record like creation, initialization, and
 * validations are working as intended.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class GenerationRecordTest : FreeSpec({
    "A [GenerationRecord]" - {
        "can be created with a generation number" {
            checkAll(Arb.generationRecord()) { record ->
                record shouldBe GenerationRecord(record.generation)
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
                checkAll(Arb.nonNegativeInt()) { generation ->
                    shouldThrowWithMessage<UninitializedPropertyAccessException>("lateinit property startTime has not been initialized") {
                        GenerationRecord(generation).startTime
                    }
                }
            }

            "can be initialized" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = GenerationRecord(data.generation)
                    val now = TimeSource.Monotonic.markNow()
                    record.startTime = now
                    record.startTime shouldBe now
                }
            }
        }

        "should have a steady generation number that" - {
            "is initialized to 0" {
                checkAll(Arb.generationRecord()) { data ->
                    val record = data
                    record.steady shouldBe 0
                }
            }

            "can be set to a non-negative integer" {
                checkAll(Arb.generationRecord(), Arb.nonNegativeInt()) { data, steady ->
                    data.steady shouldBe 0
                    data.steady = steady
                    data.steady shouldBe steady
                }
            }

            "should throw an exception if the generation number is negative" {
                checkAll(Arb.generationRecord(), Arb.negativeInt()) { data, steady ->
                    shouldThrowUnit<EnforcementException> {
                        data.steady = steady
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
                        data.population.resulting.shouldBeEmpty()
                    }
                }
                // TODO: Implement when Arb.population is implemented.
//                "can be initialized" {
//                    checkAll(Arb.generationRecord(), Arb.populationRecord()) { data, population ->
//                        val record = data
//                        record.population = population
//                        record.population shouldBe population
//                    }
//                }
            }
        }
    }
})
