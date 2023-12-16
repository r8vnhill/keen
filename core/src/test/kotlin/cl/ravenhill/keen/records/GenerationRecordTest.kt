/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.records

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.listeners.generationRecord
import cl.ravenhill.keen.arb.listeners.individualRecord
import cl.ravenhill.keen.arb.listeners.populationRecord
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.listeners.records.GenerationRecord
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.time.TestTimeSource

class GenerationRecordTest : FreeSpec({

    "A Generation Record" - {
        "can be created with a generation number" {
            checkAll(Arb.positiveInt()) { generation ->
                GenerationRecord<Int, DummyGene>(generation).apply {
                    this.generation shouldBe generation
                }
            }
        }

        "should throw an exception if the generation number is negative" {
            checkAll(Arb.negativeInt()) { generation ->
                shouldThrow<CompositeException> {
                    GenerationRecord<Int, DummyGene>(generation)
                }.shouldHaveInfringement<IntConstraintException>(
                    "The generation number [$generation] must not be negative"
                )
            }
        }

        "should have an initial time that" - {
            "is late initialized" {
                checkAll(
                    Arb.generationRecord(
                        population = Arb.populationRecord(Arb.individualRecord(Arb.genotype())),
                        startTime = null
                    )
                ) { record ->
                    shouldThrowWithMessage<UninitializedPropertyAccessException>(
                        "lateinit property startTime has not been initialized"
                    ) {
                        record.startTime
                    }
                }
            }

            "can be initialized" {
                checkAll(
                    Arb.generationRecord(population = Arb.populationRecord(Arb.individualRecord(Arb.genotype())))
                ) { record ->
                    val now = TestTimeSource().markNow()
                    record.startTime = now
                    record.startTime shouldBe now
                }
            }
        }


        "should have  steady generations number that" - {
            "is initialized to 0" {
                checkAll(
                    Arb.generationRecord(population = Arb.populationRecord(Arb.individualRecord(Arb.genotype())))
                ) { record ->
                    record.steady shouldBe 0
                }
            }

            "can be initialized to a non-negative integer" {
                checkAll(
                    Arb.generationRecord(population = Arb.populationRecord(Arb.individualRecord(Arb.genotype()))),
                    Arb.positiveInt()
                ) { record, steady ->
                    record.steady shouldBe 0
                    record.steady = steady
                    record.steady shouldBe steady
                }
            }

            "throws an exception if the generation number is negative" {
                checkAll(
                    Arb.generationRecord(population = Arb.populationRecord(Arb.individualRecord(Arb.genotype()))),
                    Arb.negativeInt()
                ) { record, steady ->
                    shouldThrowUnit<CompositeException> {
                        record.steady = steady
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The steady counter [$steady] must not be negative"
                    )
                }
            }
        }

        "should have a Population Record that" - {
            "have an offspring population that" - {
                "is initialized to an empty list" {
                    checkAll(Arb.generationRecord<Int, DummyGene>()) { record ->
                        record.population.offspring shouldBe emptyList()
                    }
                }
            }
        }
    }
})