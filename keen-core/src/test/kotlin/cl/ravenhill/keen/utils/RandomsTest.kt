/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.datatypes.arbOrderedPair
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.should.shouldBeInRange
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.`test next int in range`
import cl.ravenhill.keen.assertions.`test random char`
import cl.ravenhill.keen.assertions.`test subset`
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
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
                            arbRange(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
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
                                arbRange(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
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
                                arbRange(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
                                Arb.random()
                            ) { probability, range, random ->
                                val start = range.start
                                val end = range.endInclusive
                                val indices = random.indices(probability, end, start)
                                indices.toSet().size shouldBe indices.size
                            }
                        }
                    }

                    "should have the expected indices" {
                        checkAll(
                            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() },
                            arbRange(Arb.nonNegativeInt(5), Arb.nonNegativeInt(20)),
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
                            arbOrderedPair(Arb.int(Int.MIN_VALUE + 1..Int.MAX_VALUE), Arb.int()),
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

            "by size" - {
                "should return a list of indices that" - {
                    "should have the expected size" {
                        checkAll(
                            arbOrderedPair(Arb.int(0..100), strict = true),
                            Arb.int(0..10),
                            Arb.random()
                        ) { (start, end), size, random ->
                            assume { size shouldBeInRange 0..(end - start) }
                            val indices = random.indices(size, end, start)
                            indices.size shouldBe size
                        }
                    }

                    "should have indices that" - {
                        "should be in the range [start, end)" {
                            checkAll(
                                arbOrderedPair(Arb.int(0..100), strict = true),
                                Arb.int(0..10),
                                Arb.random()
                            ) { (start, end), size, random ->
                                assume { size shouldBeInRange 0..(end - start) }
                                val indices = random.indices(size, end, start)
                                indices.forEach { it shouldBeInRange start..<end }
                            }
                        }

                        "should be in range [0, end) if start is not specified" {
                            checkAll(
                                Arb.int(1..100),
                                Arb.int(0..10),
                                Arb.random()
                            ) { end, size, random ->
                                assume { size shouldBeInRange 0..end }
                                val indices = random.indices(size, end)
                                indices.forEach { it shouldBeInRange 0..<end }
                            }
                        }

                        "should be unique" {
                            checkAll(
                                arbOrderedPair(Arb.int(0..100), strict = true),
                                Arb.int(0..10),
                                Arb.random()
                            ) { (start, end), size, random ->
                                assume { size shouldBeInRange 0..(end - start) }
                                val indices = random.indices(size, end, start)
                                indices.toSet().size shouldBe indices.size
                            }
                        }
                    }
                }

                "should throw an exception if" - {
                    "size is less than zero" {
                        checkAll(
                            Arb.negativeInt(),
                            Arb.int(),
                            Arb.int(),
                            Arb.random()
                        ) { size, start, end, random ->
                            shouldThrow<CompositeException> {
                                random.indices(size, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The size ($size) must be greater than or equal to 0"
                            )
                        }
                    }

                    "size is greater than the range" {
                        checkAll(
                            arbOrderedPair(Arb.int(0..100), strict = true),
                            Arb.positiveInt(),
                            Arb.random()
                        ) { (start, end), size, random ->
                            assume { size shouldBeGreaterThan (end - start) }
                            shouldThrow<CompositeException> {
                                random.indices(size, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The size ($size) must be at most the size of the range (${end - start})."
                            )
                        }
                    }

                    "end is less than zero" {
                        checkAll(
                            Arb.negativeInt(),
                            Arb.int(),
                            Arb.int(),
                            Arb.random()
                        ) { end, size, start, random ->
                            shouldThrow<CompositeException> {
                                random.indices(size, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The end index ($end) must be greater than or equal to 0."
                            )
                        }
                    }

                    "start is less than zero" {
                        checkAll(
                            Arb.negativeInt(),
                            Arb.int(),
                            Arb.int(),
                            Arb.random()
                        ) { start, size, end, random ->
                            shouldThrow<CompositeException> {
                                random.indices(size, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The start index ($start) must be greater than or equal to 0."
                            )
                        }
                    }

                    "end is less than start" {
                        checkAll(
                            arbOrderedPair(Arb.int(Int.MIN_VALUE + 1..Int.MAX_VALUE), Arb.int()),
                            Arb.int(),
                            Arb.random()
                        ) { (end, start), size, random ->
                            shouldThrow<CompositeException> {
                                random.indices(size, end, start)
                            }.shouldHaveInfringement<IntConstraintException>(
                                "The start index ($start) must be less than the end index ($end)."
                            )
                        }
                    }
                }
            }
        }

        `test subset`()

        "when generating a random double within a range" - {
            "should return a value within the range" {
                checkAll(
                    Arb.random(),
                    arbOrderedPair(Arb.double().filterNot { it.isNaN() || it.isInfinite() })
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
                    arbOrderedPair(Arb.double().filterNot { it.isNaN() || it.isInfinite() })
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