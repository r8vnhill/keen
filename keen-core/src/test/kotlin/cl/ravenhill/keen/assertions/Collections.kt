/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.any
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

suspend fun `test invalid index in swap`(
    swapOperation: (MutableList<Any>, Int, Int) -> Unit,
    errorMessage: (Int, IntRange) -> String,
) {
    checkAll(Arb.list(Arb.any()).map { list ->
        val idx = Arb.int().filterNot { it in list.indices }.next()
        idx to list.toMutableList()
    }, Arb.int()) { (invalidIndex, list), validIndex ->
        shouldThrow<CompositeException> {
            swapOperation(list, invalidIndex, validIndex)
        }.shouldHaveInfringement<IntConstraintException>(
            errorMessage(invalidIndex, list.indices)
        )
    }
}
