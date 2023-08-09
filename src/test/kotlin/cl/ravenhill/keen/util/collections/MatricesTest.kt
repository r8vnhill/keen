/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.collections

import cl.ravenhill.any
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.keen.shouldBeOfClass
import cl.ravenhill.keen.util.shouldAny
import cl.ravenhill.keen.util.transpose
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll


class MatricesTest : FreeSpec({


    "Transposing a list of lists should" - {
        "return an empty list if the list is empty" {
            emptyList<List<Any>>().transpose() shouldBe emptyList()
        }

        "return a list of lists with the elements at the same indices" {
            checkAll(Arb.matrix(Arb.any())) { matrix ->
                val transposed = matrix.transpose()
                assertSoftly {
                    transposed.forEachIndexed { i, list ->
                        list.forEachIndexed { j, e ->
                            e shouldBe matrix[j][i]
                        }
                    }
                }
            }
        }

        "throw an exception if the lists are not of the same size" {
            checkAll(Arb.list(Arb.list(Arb.any()))) { ass ->
                assume {
                    ass.shouldNotBeEmpty()
                    ass shouldAny { it.size != ass.first().size }
                }
                shouldThrow<EnforcementException> {
                    ass.transpose()
                }.infringements.first() shouldBeOfClass UnfulfilledRequirementException::class
            }
        }
    }
})


/**
 * Returns an [Arb] that generates a matrix with random elements of type [T].
 * The number of rows and columns of the matrix are randomly generated between 1 and 100.
 */
private fun <T> Arb.Companion.matrix(gen: Arb<T>) = arbitrary {
    val rows = int(1..50).bind()
    val cols = int(1..50).bind()
    List(rows) { List(cols) { gen.bind() } }
}