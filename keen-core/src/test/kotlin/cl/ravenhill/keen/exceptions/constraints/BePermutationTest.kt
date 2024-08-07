/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions.constraints

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class BePermutationTest : FreeSpec({

    "A BePermutation constraint" - {
        "should have a validator function that" - {
            "returns true when the collection is a permutation" {
                checkAll(permutation()) { list ->
                    BePermutation.validator(list).shouldBeTrue()
                }
            }

            "returns false when the collection is not a permutation" {
                checkAll(nonPermutation()) { list ->
                    BePermutation.validator(list).shouldBeFalse()
                }
            }
        }
    }
})

private fun permutation(): Arb<List<Int>> = Arb.list(Arb.int()).filter { it.distinct().size == it.size }

private fun nonPermutation(): Arb<List<Int>> = Arb.list(Arb.int()).filter { it.distinct().size != it.size }