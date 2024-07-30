/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.matchers.shouldHaveInfringement
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

typealias SimpleGenerationRecord = GenerationRecord<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>
typealias GenerationRecordArb<T, F> = Arb<GenerationRecord<T, F, Representation<T, F>>>

class GenerationRecordTest : FreeSpec({
    "A GenerationRecord" - {
        "can be created with a generation number" {
            checkAll(Arb.nonNegativeInt()) { generation ->
                val record = SimpleGenerationRecord(generation)
                record.generation shouldBe generation
            }
        }

        "throws an exception when created with a negative generation number" {
            checkAll(Arb.negativeInt()) { generation ->
                shouldThrow<CompositeException> {
                    SimpleGenerationRecord(generation)
                }.shouldHaveInfringement<IntConstraintException>(
                    "The generation number ($generation) must not be negative"
                )
            }
        }

        "should have a steady counter that" - {
            "starts at 0" {
                checkAll(
                    arbGenerationRecord<Int, SimpleFeature<Int>>()
                ) { record ->
                    record.steady shouldBe 0
                }
            }

            "can be set to a non-negative value" {
                checkAll(
                    arbGenerationRecord<Int, SimpleFeature<Int>>(),
                    Arb.nonNegativeInt()
                ) { record, value ->
                    record.steady = value
                    record.steady shouldBe value
                }
            }

            "throws an exception when set to a negative value" {
                checkAll(
                    arbGenerationRecord<Int, SimpleFeature<Int>>(),
                    Arb.negativeInt()
                ) { record, value ->
                    shouldThrowUnit<CompositeException> {
                        record.steady = value
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The steady counter ($value) must not be negative"
                    )
                }
            }
        }
    }
})

fun <T, F> arbGenerationRecord(
    generation: Arb<Int> = Arb.nonNegativeInt()
): GenerationRecordArb<T, F> where F : Feature<T, F> = generation.map { GenerationRecord(it) }
