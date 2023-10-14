package cl.ravenhill.keen.util

import cl.ravenhill.any
import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.keen.random
import cl.ravenhill.orderedPair
import cl.ravenhill.orderedTriple
import cl.ravenhill.real
import cl.ravenhill.unfulfilledConstraint
import cl.ravenhill.utils.toRange
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.char.shouldBeInRange
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.ints.shouldBePositive
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

/**
 * Returns an [Arb] that generates random [CharRange] objects.
 */
private fun Arb.Companion.charRange() = arbitrary {
    val (lo, hi) = orderedPair(char(), char()).bind()
    lo..hi
}

/**
 * Returns an arbitrary that generates a random filter function for [Char] values.
 * The function will randomly return `true`, or filter by letters, digits, uppercase letters, or
 * lowercase letters.
 * The returned function has the signature `(Char) -> Boolean`.
 */
private fun Arb.Companion.filter() = arbitrary {
    element(
        { _: Char -> true },
        { c: Char -> c.isLetter() },
        { c: Char -> c.isDigit() },
        { c: Char -> c.isUpperCase() },
        { c: Char -> c.isLowerCase() }
    ).bind()
}

class RandomsTest : FreeSpec({
    "Generating a random character should" - {
        "return a character in a given range" {
            checkAll(Arb.charRange(), Arb.random()) { range, rng ->
                rng.nextChar(range) shouldBeInRange range
            }
        }

        "return a character that satisfies the filter" {
            checkAll(Arb.filter(), Arb.random()) { filter, rng ->
                val char = rng.nextChar(' '..'~', filter)
                char shouldBeInRange ' '..'~'
                filter(char) shouldBe true
            }
        }

        "return a character in range [0, 0xFFFF] if no range is given" {
            checkAll(Arb.random()) { rng ->
                val char = rng.nextChar()
                char shouldBeInRange '\u0000'..'\uFFFF'
            }
        }
    }

    "Generating a random string" - {
        "with default values should" - {
            "return a string of random length between 1 and 10" {
                checkAll(Arb.random()) { rng ->
                    val string = rng.nextString()
                    string.length shouldBeInRange 1..10
                }
            }

            "return a string with all characters in range [0, 0xFFFF]" {
                checkAll(Arb.random()) { rng ->
                    val string = rng.nextString()
                    string.forEach { it shouldBeInRange '\u0000'..'\uFFFF' }
                }
            }
        }

        "with a given length should return a string of the given length" {
            checkAll(Arb.positiveInt(100), Arb.random()) { length, rng ->
                val string = rng.nextString(length)
                string.length shouldBe length
            }
        }

        "with a given range should return a string with characters in the given range" {
            checkAll(Arb.charRange(), Arb.random()) { range, rng ->
                val string = rng.nextString(range = range)
                string.forEach { it shouldBeInRange range }
            }
        }

        "with a given filter should return a string with characters that satisfy the filter" {
            checkAll(Arb.filter(), Arb.random()) { filter, rng ->
                val string = rng.nextString(filter = filter)
                string.forEach { filter(it) shouldBe true }
            }
        }
    }

    "Generating a random integer in a given range should return an integer in the given range" {
        with(Arb) {
            checkAll(
                orderedPair(int(), int(), strict = true),
                random()
            ) { orderedPair, randomGenerator ->
                val range = orderedPair.toRange()
                randomGenerator.nextIntInRange(range) shouldBeInRange range
            }
        }
    }

    "Generating random indices" - {
        "by probability should" - {
            "return an empty sequence if the pick probability is too low" {
                checkAll(
                    Arb.real(0.0..1e-20),
                    Arb.orderedPair(Arb.int(1_000_000), Arb.int(1_000_000)),
                    Arb.random()
                ) { pickProbability, (lo, hi), rng ->
                    assume {
                        lo shouldNotBe hi
                    }
                    rng.indices(pickProbability, hi, lo).toList() shouldBe emptyList()
                }
            }

            "return a sequence of all indices if the pick probability is too high" {
                checkAll(
                    Arb.orderedPair(Arb.int(0, 10_000), Arb.int(0, 10_000)), Arb.random()
                ) { (lo, hi), rng ->
                    val pickProbability = 1 - 1e-20
                    rng.indices(pickProbability, hi, lo).toList() shouldBe (lo until hi).toList()
                }
            }

            "return a sequence of random indices in the given range" {
                checkAll(
                    Arb.real(1e-20..1 - 1e-20),
                    Arb.orderedPair(Arb.int(0, 10_000), Arb.int(0, 10_000)),
                    Arb.random()
                ) { pickProbability, (lo, hi), rng ->
                    assume {
                        lo shouldNotBe hi
                    }
                    val indices = rng.indices(pickProbability, hi, lo).toList()
                    indices.forEach { it shouldBeInRange lo..hi }
                }
            }

            "return a sequence of random indices in the range [0, size) by default" {
                checkAll(
                    Arb.real(1e-20..1 - 1e-20), Arb.int(0..100), Arb.random()
                ) { pickProbability, hi, rng ->
                    val indices = rng.indices(pickProbability, hi).toList()
                    indices.forEach { it shouldBeInRange 0..hi }
                }
            }
        }

        "by size should" - {
            "return a list of random indices in the given range" {
                checkAll(
                    Arb.orderedTriple(Arb.int(0, 10_000), Arb.int(0, 10_000), Arb.int(0, 10_000)),
                    Arb.random()
                ) { (lo, mid, hi), rng ->
                    assume {
                        lo shouldNotBe mid
                        mid shouldNotBe hi
                    }
                    val indices = rng.indices(mid - lo, hi, lo)
                    indices.forEach { it shouldBeInRange lo..hi }
                }
            }

            "return a list of random indices in the range [0, size) by default" {
                checkAll(
                    Arb.orderedPair(Arb.int(0, 100), Arb.int(0, 100)), Arb.random()
                ) { (size, hi), rng ->
                    val indices = rng.indices(size, hi)
                    indices.forEach { it shouldBeInRange 0..hi }
                }
            }
        }
    }

    "Generating subsets should" - {
        "return a list of random subsets of the given size containing all elements" {
            checkAll(Arb.listAndIndex(Arb.any()), Arb.random()) { (elements, size), rng ->
                assume {
                    size.shouldBePositive()
                }
                val subsets = rng.subsets(elements, size, false)
                assertSoftly {
                    subsets.forEach { it.size shouldBe size }
                    subsets.flatten() shouldContainAll elements
                }
            }
        }

        "return a list of random subsets of the given size containing all elements without duplicates if exclusivity is true" {
            checkAll(
                Arb.listAndIndex(Arb.any(), 1..100) { Arb.divisor(it.size) },
                Arb.random()
            ) { (elements, size), rng ->
                assume {
                    size.shouldBePositive()
                }
                val subsets = rng.subsets(elements, size, true)
                assertSoftly {
                    subsets.forEach { it.size shouldBe size }
                    subsets.flatten().size shouldBe elements.size
                    subsets.flatten() shouldContainAll elements
                }
            }
        }

        "return a list of at most the given number of subsets" {
            checkAll(
                Arb.listAndIndex(Arb.any(), 1..100),
                Arb.positiveInt(),
                Arb.random()
            ) { (elements, size), limit, rng ->
                assume {
                    size.shouldBePositive()
                }
                val subsets = rng.subsets(elements, size, false, limit)
                subsets.size shouldBeLessThanOrEqual limit
            }
        }

        "throw an exception if" - {
            "the elements list is empty" {
                checkAll(
                    Arb.positiveInt(), Arb.boolean(), Arb.random()
                ) { size, exclusivity, rng ->
                    val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                        rng.subsets(emptyList<Any>(), size, exclusivity)
                    }
                    ex.infringements.size shouldBe 1
                    with(ex.infringements.first()) {
                        shouldBeInstanceOf<cl.ravenhill.enforcer.CollectionRequirementException>()
                        message shouldBe unfulfilledConstraint("The input list must not be empty.")
                    }
                }
            }

            "the subsets size is non-positive" {
                checkAll(
                    Arb.list(Arb.any(), 1..100), Arb.nonPositiveInt(), Arb.boolean(), Arb.random()
                ) { elements, size, exclusivity, rng ->
                    val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                        rng.subsets(elements, size, exclusivity)
                    }
                    with(ex.infringements.first()) {
                        shouldBeInstanceOf<cl.ravenhill.enforcer.IntRequirementException>()
                        message shouldBe unfulfilledConstraint("The subset size [$size] must be at least 1 and at most the number of elements in the input list [${elements.size}].")
                    }
                }
            }

            "the elements list is too small" {
                checkAll(
                    Arb.list(Arb.any(), 1..100), Arb.positiveInt(), Arb.boolean(), Arb.random()
                ) { elements, size, exclusivity, rng ->
                    assume {
                        elements.size shouldBeLessThan size
                    }
                    val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                        rng.subsets(elements, size, exclusivity)
                    }
                    with(ex.infringements.first()) {
                        shouldBeInstanceOf<cl.ravenhill.enforcer.IntRequirementException>()
                        message shouldBe unfulfilledConstraint("The subset size [$size] must be at least 1 and at most the number of elements in the input list [${elements.size}].")
                    }
                }
            }

            "the elements list's size is not valid when exclusivity is required" {
                checkAll(
                    Arb.listAndIndex(Arb.any(), 1..1000),
                    Arb.random()
                ) { (elements, size), rng ->
                    assume {
                        size.shouldBePositive()
                        elements.size shouldNotBeMultipleOf size
                    }
                    val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                        rng.subsets(elements, size, true)
                    }
                    with(ex.infringements.first()) {
                        shouldBeInstanceOf<UnfulfilledRequirementException>()
                        message shouldBe unfulfilledConstraint(
                            "The number of elements [${elements.size}] must be a " +
                                "multiple of the subset size [$size] when using exclusive " +
                                "subsets."
                        )
                    }
                }
            }

            "the limit is non-positive" {
                checkAll(
                    Arb.list(Arb.any(), 1..100), Arb.nonPositiveInt(), Arb.boolean(), Arb.random()
                ) { elements, limit, exclusivity, rng ->
                    val ex = shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                        rng.subsets(elements, elements.size, exclusivity, limit)
                    }
                    with(ex.infringements.first()) {
                        shouldBeInstanceOf<cl.ravenhill.enforcer.IntRequirementException>()
                        message shouldBe unfulfilledConstraint("The limit [$limit] must be at least 1.")
                    }
                }
            }
        }
    }
})

/**
 * Asserts that the integer is a multiple of the given number.
 */
private infix fun Int.shouldNotBeMultipleOf(n: Int) = should(
    Matcher { value ->
        MatcherResult(
            value % n != 0,
            { "$value should not be a multiple of $n" },
            { "$value should be a multiple of $n" }
        )
    }
)

/**
 * Generates a pair consisting of a random list and a random index within the list.
 */
private fun <E> Arb.Companion.listAndIndex(
    listGen: Arb<E>,
    range: IntRange = 0..100,
    indexGen: (List<E>) -> Arb<Int> = { Arb.int(0..it.size) },
) = arbitrary {
    val list = list(listGen, range).bind()
    val index = indexGen(list).bind()
    list to index
}

private fun Arb.Companion.divisor(number: Int) = arbitrary { rs ->
    val divisors = (1..number).filter { number % it == 0 }
    if (divisors.isNotEmpty()) {
        val randomDivisor = divisors.random(rs.random)
        randomDivisor
    } else {
        1
    }
}
