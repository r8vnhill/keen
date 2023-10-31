/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.collections

import cl.ravenhill.keen.util.incremental
import cl.ravenhill.keen.util.shouldEq
import cl.ravenhill.keen.arbs.real
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll


/**
 * A test suite for validating various operations on arrays.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class ArraysTest : FreeSpec({
    "An array can be transformed into an incremental array" {
        checkAll(Arb.list(Arb.real(0.0..100_000.0))) { ds ->
            val array = ds.toDoubleArray()
            val copy = array.copyOf()
            array.incremental()
            assertSoftly {
                array.forEachIndexed { i, d ->
                    d shouldEq copy.sumFirst(i + 1)
                }
            }
        }
    }
})


/**
 * Returns the sum of the first [n] elements of this [DoubleArray].
 */
private fun DoubleArray.sumFirst(n: Int) = this.take(n).sum()