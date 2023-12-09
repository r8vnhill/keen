/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import cl.ravenhill.keen.arb.genetic.datatypes.orderedPair
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.shouldBeInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class RandomsTest : FreeSpec({

    "A Random number generator" - {
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