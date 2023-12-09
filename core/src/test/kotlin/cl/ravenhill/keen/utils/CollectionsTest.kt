/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.any
import cl.ravenhill.keen.arb.matrix
import cl.ravenhill.keen.assertions.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class CollectionsTest : FreeSpec({

    "A List" - {
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