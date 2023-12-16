/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.datatypes.orderedPair
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.arb.range
import cl.ravenhill.keen.assertions.should.shouldBeInRange
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.`test next int in range`
import cl.ravenhill.keen.assertions.`test random char`
import cl.ravenhill.keen.assertions.`test subset`
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class RandomsTest : FreeSpec({

    "A Random number generator" - {

        `test random char`()

        `test next int in range`()

        "when picking random indices" - {
            "by pick probability" - {
                "should return a list of indices that" - {
                    "should have the expected size" {
                        checkAll(
                            PropTestConfig(maxFailure = 300, minSuccess = 700),
                            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.range(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
                            Arb.random()
                        ) { probability, range, random ->
                            val start = range.start
                            val end = range.endInclusive
                            val indices = random.indices(probability, end, start)
                            indices.size shouldBeInRange ((end - start) * probability).let {
                                it.toInt() - 1..it.toInt() + 1
                            }
                        }
                    }

                    "should have indices that" - {
                        "should be in the range [start, end)" {
                            checkAll(
                                Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                                Arb.range(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
                                Arb.random()
                            ) { probability, range, random ->
                                val start = range.start
                                val end = range.endInclusive
                                val indices = random.indices(probability, end, start)
                                indices.forEach { it shouldBeInRange start..<end }
                            }
                        }

                        "should be unique" {
                            checkAll(
                                Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                                Arb.range(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
                                Arb.random()
                            ) { probability, range, random ->
                                val start = range.start
                                val end = range.endInclusive
                                val indices = random.indices(probability, end, start)
                                indices.toSet().size shouldBe indices.size
                            }
                        }

                        "should be sorted" {
                            checkAll(
                                Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                                Arb.range(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
                                Arb.random()
                            ) { probability, range, random ->
                                val start = range.start
                                val end = range.endInclusive
                                val indices = random.indices(probability, end, start)
                                indices shouldBe indices.sorted()
                            }
                        }
                    }

                    "should have the expected indices" {
                        checkAll(
                            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.range(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
                            Arb.long().map { Random(it) to Random(it) }
                        ) { probability, range, (rng1, rng2) ->
                            val start = range.start
                            val end = range.endInclusive
                            val indices = rng1.indices(probability, end, start)
                            val expectedIndices = (start..<end).filter { rng2.nextDouble() < probability }
                            indices shouldBe expectedIndices
                        }
                    }
                }

                "should throw an exception if" - {
                    "pick probability is not in the range [0, 1]" {
                        checkAll(
                            Arb.double().filterNot { it in 0.0..1.0 },
                            Arb.int(),
                            Arb.int(),
                            Arb.random()
                        ) { probability, start, end, random ->
                            shouldThrow<CompositeException> {
                                random.indices(probability, end, start)
                            }.shouldHaveInfringement<DoubleConstraintException>(
                                "The pick probability ($probability) must be in the range ${0.0..1.0}"
                            )
                        }
                    }

                    "end is less than or equal to start" {
                        checkAll(
                            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.orderedPair(Arb.int(Int.MIN_VALUE + 1..Int.MAX_VALUE), Arb.int()),
                            Arb.random()
                        ) { probability, (end, start), random ->
                            shouldThrow<CompositeException> {
                                random.indices(probability, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The end ($end) must be greater than or equal to the start ($start)"
                            )
                        }
                    }

                    "end index is less than zero" {
                        checkAll(
                            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.int(Int.MIN_VALUE..-1),
                            Arb.int(),
                            Arb.random()
                        ) { probability, end, start, random ->
                            shouldThrow<CompositeException> {
                                random.indices(probability, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The end index ($end) must be greater than or equal to 0"
                            )
                        }
                    }

                    "start index is less than zero" {
                        checkAll(
                            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.int(),
                            Arb.int(Int.MIN_VALUE..-1),
                            Arb.random()
                        ) { probability, end, start, random ->
                            shouldThrow<CompositeException> {
                                random.indices(probability, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The start index ($start) must be greater than or equal to 0"
                            )
                        }
                    }
                }
            }

            "by size" - { fail("Not implemented yet") }
        }

        `test subset`()

        "when generating a random double within a range" - {
            "should return a value within the range" {
                checkAll(
                    Arb.random(),
                    Arb.orderedPair(Arb.double().filterNot { it.isNaN() || it.isInfinite() })
                        .filter { (lo, hi) -> lo < hi }
                ) { random, (lo, hi) ->
                    val range = lo..hi
                    val value = random.nextDoubleInRange(range)
                    value shouldBeInRange range
                }
            }

            "should return the expected value" {
                checkAll(
                    Arb.long(),
                    Arb.orderedPair(Arb.double().filterNot { it.isNaN() || it.isInfinite() })
                        .filter { (lo, hi) -> lo < hi }
                ) { seed, (lo, hi) ->
                    val range = lo..hi
                    val value = Random(seed).nextDoubleInRange(range)
                    value shouldBe Random(seed).nextDouble(lo, hi)
                }
            }
        }
    }
})