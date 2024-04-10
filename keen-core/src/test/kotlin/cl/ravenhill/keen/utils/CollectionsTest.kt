/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.any
import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.datatypes.arbOrderedPair
import cl.ravenhill.keen.arb.matrix
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.`test invalid index in swap`
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll

class CollectionsTest : FreeSpec({

    "An Iterable of Doubles" - {
        "when subtracting a constant value" - {
            "should return a new list with the result of the subtraction" {
                checkAll(Arb.list(Arb.double(), 0..10), Arb.double()) { list, subtrahend ->
                    list.sub(subtrahend).apply {
                        size shouldBe list.size
                        for (i in indices) {
                            this[i] shouldBe list[i] - subtrahend
                        }
                    }
                }
            }
        }
    }

    "A List" - {
        "when accessing a sublist" - {
            "should return a list with the elements in the specified range" {
                checkAll(Arb.list(Arb.any(), 2..10).map { list ->
                    val range = arbRange(Arb.int(list.indices), Arb.int(list.indices)).next()
                    range to list
                }) { (range, list) ->
                    list[range].apply {
                        size shouldBe range.endInclusive - range.start + 1
                        for (i in indices) {
                            this[i] shouldBe list[range.start + i]
                        }
                    }
                }
            }

            "should throw an exception if the list is empty" {
                checkAll(arbRange(Arb.int(), Arb.int())) { range ->
                    shouldThrow<CompositeException> {
                        emptyList<Any>()[range]
                    }.shouldHaveInfringement<CollectionConstraintException>("The list must not be empty")
                }
            }

            "should throw an exception if the start index is not in the list indices" {
                checkAll(
                    Arb.list(Arb.any(), 1..10).map { list ->
                        val start = Arb.int().filterNot { it in list.indices }.next()
                        start to list
                    }, Arb.int()
                ) { (start, list), end ->
                    shouldThrow<CompositeException> {
                        list[start..end]
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The start index ($start) must be in range ${list.indices}"
                    )
                }
            }

            "should throw an exception if the end index is not in the list indices" {
                checkAll(
                    Arb.list(Arb.any(), 1..10).map { list ->
                        val end = Arb.int().filterNot { it in list.indices }.next()
                        end to list
                    }, Arb.int()
                ) { (end, list), start ->
                    shouldThrow<CompositeException> {
                        list[start..end]
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The end index ($end) must be in range ${list.indices}"
                    )
                }
            }

            "should throw an exception if the start index is greater than the end index" {
                checkAll(
                    Arb.list(Arb.any(), 1..10),
                    arbOrderedPair(Arb.int(), Arb.int(), strict = true, reversed = true)
                ) { list, (start, end) ->
                    shouldThrow<CompositeException> {
                        list[start..end]
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The start index ($start) must be less than or equal to the end index ($end)"
                    )
                }
            }
        }

        "when swapping two elements" - {
            "should swap the elements at the given indices" {
                checkAll(Arb.list(Arb.any(), 2..10).map { list ->
                    list.toMutableList() to
                          arbRange(Arb.int(list.indices), Arb.int(list.indices)).next()
                }) { (list, range) ->
                    val expected = list.toList()
                    list.swap(range.start, range.endInclusive)
                    list[range.start] shouldBe expected[range.endInclusive]
                    list[range.endInclusive] shouldBe expected[range.start]
                }
            }

            "should not change the list if the indices are the same" {
                checkAll(Arb.list(Arb.any(), 1..10).map { list ->
                    val i = Arb.int(list.indices).next()
                    i to list.toMutableList()
                }) { (i, list) ->
                    val expected = list.toList()
                    list.swap(i, i)
                    list shouldBe expected
                }
            }

            "should throw an exception if the list is empty" {
                checkAll(Arb.int(), Arb.int()) { i, j ->
                    shouldThrow<CompositeException> {
                        mutableListOf<Any>().swap(i, j)
                    }.shouldHaveInfringement<CollectionConstraintException>("The list must not be empty")
                }
            }

            "should throw an exception if the first index is not in the list indices" {
                `test invalid index in swap`(
                    { list, invalid, valid -> list.swap(invalid, valid) },
                    { i, indices -> "The first index ($i) must be in range $indices" }
                )
            }

            "should throw an exception if the second index is not in the list indices" {
                `test invalid index in swap`(
                    { list, invalid, valid -> list.swap(valid, invalid) },
                    { i, indices -> "The second index ($i) must be in range $indices" }
                )
            }
        }

        "of Doubles can be converted to an incremental list" {
            checkAll(Arb.list(Arb.double(-100.0..100.0).filterNot { it.isNaN() || it.isInfinite() }, 0..10)) { list ->
                list.incremental().apply {
                    size shouldBe list.size
                    // check that all elements are the sum of the previous ones
                    for (i in 1..<size) {
                        this[i] shouldBe this[i - 1] + list[i]
                    }
                }
            }
        }

        "of lists" - {
            "when transposed" - {
                "should have the same size as the first element" {
                    checkAll(
                        Arb.matrix(
                            Arb.double(-100.0..100.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.int(1..10),
                            Arb.int(1..10)
                        )
                    ) { list ->
                        list.transpose().apply {
                            size shouldBe list[0].size
                        }
                    }
                }

                "should return a list of lists with the same elements" {
                    checkAll(
                        Arb.matrix(
                            Arb.double(-100.0..100.0).filterNot { it.isNaN() || it.isInfinite() },
                            Arb.int(1..10),
                            Arb.int(1..10)
                        )
                    ) { matrix ->
                        matrix.transpose().apply {
                            for (i in indices) {
                                this[i].size shouldBe matrix.size
                                for (j in this[i].indices) {
                                    this[i][j] shouldBe matrix[j][i]
                                }
                            }
                        }
                    }
                }

                "should return an empty list when the original list is empty" {
                    emptyList<List<Double>>().transpose().apply {
                        size shouldBe 0
                    }
                }

                "should throw an exception if the lists have different" {
                    checkAll(Arb.list(Arb.list(Arb.any(), 1..10), 1..10)) { ass ->
                        assume {
                            ass.distinctBy { it.size }.size shouldBeGreaterThan 1
                        }
                        shouldThrow<CompositeException> {
                            ass.transpose()
                        }.shouldHaveInfringement<CollectionConstraintException>("All lists must have the same size")
                    }
                }
            }
        }
    }
})
