package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.char.shouldBeInRange
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


class RandomsSpec : WordSpec({
    "Generating a random character" should {
        "return a character in the range of printable characters" {
            checkAll<Long> { seed ->
                Core.rng = Random(seed)
                Core.rng.nextChar() shouldBeInRange
                        0.toChar()..Character.MAX_CODE_POINT.toChar()
            }
        }
    }
    "Generating random indices" should {
        "return an empty sequence if the pick probability is too low" {
            checkAll(
                Arb.double(0.0, 1e-20),
                Arb.int(1_000_000),
                Arb.int(1_000_000),
                Arb.long()
            ) { pickProbability, i1, i2, seed ->
                assume(
                    pickProbability != Double.POSITIVE_INFINITY
                            && pickProbability != Double.NEGATIVE_INFINITY
                )
                assume(!pickProbability.isNaN())
                assume(i1 != i2)
                Core.rng = Random(seed)
                val (lo, hi) = if (i1 < i2) i1 to i2 else i2 to i1
                Core.rng.indices(pickProbability, hi, lo).toList() shouldBe emptyList()
            }
        }
        "return a sequence of all indices if the pick probability is too high" {
            checkAll(
                Arb.int(0, 10_000),
                Arb.int(0, 10_000),
                Arb.long()
            ) { i1, i2, seed ->
                val pickProbability = 1 - 1e-20
                Core.rng = Random(seed)
                val (lo, hi) = if (i1 < i2) i1 to i2 else i2 to i1
                Core.rng.indices(pickProbability, hi, lo)
                    .toList() shouldBe (lo until hi).toList()
            }
        }
        "return a sequence of random indices in the given range" {
            checkAll(
                Arb.double(1e-20, 1 - 1e-20),
                Arb.int(0, 10_000),
                Arb.int(0, 10_000),
                Arb.long()
            ) { pickProbability, i1, i2, seed ->
                assume(
                    pickProbability != Double.POSITIVE_INFINITY
                            && pickProbability != Double.NEGATIVE_INFINITY
                )
                assume(!pickProbability.isNaN())
                assume(i1 != i2)
                Core.rng = Random(seed)
                val (lo, hi) = if (i1 < i2) i1 to i2 else i2 to i1
                val indices = Core.rng.indices(pickProbability, hi, lo).toList()
                indices.forEach { it shouldBeInRange lo..hi }
            }
        }
    }
    "Generating a random integer outside of a range" should {
        "return an integer outside of the given range" {
            checkAll(
                Arb.int(0, 10_000),
                Arb.int(0, 10_000),
                Arb.long()
            ) { i1, i2, seed ->
                assume(i1 != i2)
                Core.rng = Random(seed)
                val (lo, hi) = if (i1 < i2) i1 to i2 else i2 to i1
                val outside = Core.rng.nextIntOutsideOf(lo..hi)
                ((outside in (Int.MIN_VALUE until lo))
                        || (outside in (hi + 1..Int.MAX_VALUE))) shouldBe true
            }
        }
    }
})