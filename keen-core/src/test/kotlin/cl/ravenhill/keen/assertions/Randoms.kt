/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.any
import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.datatypes.arbDivisor
import cl.ravenhill.keen.arb.datatypes.arbOrderedPair
import cl.ravenhill.keen.arb.random
import cl.ravenhill.keen.assertions.should.shouldBeInRange
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.utils.indices
import cl.ravenhill.keen.utils.nextChar
import cl.ravenhill.keen.utils.nextIntInRange
import cl.ravenhill.keen.utils.subsets
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

/**
 * Tests for the `nextChar` extension function on the `Random` class.
 *
 * This function is structured as a series of nested tests using Kotest's FreeSpec style. Each nested test
 * covers a different aspect of the `nextChar` function's behavior, ensuring its correctness and reliability
 * under various conditions.
 *
 * ## Test Structure:
 * - **Test 1: Default Range**
 *   Validates that invoking `nextChar` without specifying a range produces a character within the full
 *   Unicode character range (`Char.MIN_VALUE..Char.MAX_VALUE`).
 *
 * - **Test 2: Explicit Range**
 *   Checks that `nextChar`, when provided with an explicit character range, returns a character within that
 *   specified range.
 *
 * - **Test 3: Default Filter with Range**
 *   Confirms that calling `nextChar` without an explicit filter, but with a specified range, results in a
 *   character within the provided range.
 *
 * - **Test 4: Explicit Filter**
 *   Ensures that `nextChar`, when used with an explicit filter, returns a character that adheres to the
 *   filter criteria. This test uses Kotest's `eventually` to repeatedly evaluate the condition within a
 *   given timeframe.
 *
 * ## Usage:
 * This testing function is used as part of a test suite for the `Random` class extension functions. It
 * employs property-based testing techniques to ensure a comprehensive evaluation across a wide range
 * of inputs.
 */
suspend fun FreeSpecContainerScope.`test random char`() {
    "when generating a char" - {
        "without an explicit range should return a value within the full range" {
            checkAll(Arb.random()) { random ->
                val value = random.nextChar()
                value shouldBeInRange Char.MIN_VALUE..Char.MAX_VALUE
            }
        }

        "with an explicit range should return a value within the range" {
            checkAll(Arb.random(), arbRange(Arb.char(), Arb.char()).filterNot { it.isEmpty() }) { random, range ->
                val value = random.nextChar(range)
                value shouldBeInRange range
            }
        }

        "without an explicit filter should return a value within the provided range" {
            checkAll(Arb.random(), arbRange(Arb.char(), Arb.char()).filterNot { it.isEmpty() }) { random, range ->
                val value = random.nextChar { it in range }
                value shouldBeInRange range
            }
        }

        "with an explicit filter should return a value within the provided range" {
            eventually(1.milliseconds) {
                Random.nextChar { !it.isUpperCase() }.isUpperCase().shouldBeFalse()
            }
        }
    }
}

/**
 * Tests the functionality of the `nextIntInRange` extension function for the `Random` class.
 *
 * This test is structured using Kotest's FreeSpec style and focuses on validating the behavior of
 * `nextIntInRange`. It ensures that the function correctly generates integers within a specified range.
 *
 * ## Test Description:
 * - **Generating an Int Within a Range**: The test verifies that when `nextIntInRange` is called with a given
 *   integer range, the integer returned is always within the specified range. It uses property-based testing to
 *   cover a wide range of possible ranges.
 *
 * ## Test Methodology:
 * - The test uses `checkAll` from Kotest's property-based testing arsenal. It generates random instances and
 *   various ranges of integers where the start is less than the end.
 * - For each generated pair of a `Random` instance and an integer range, the test checks if the value returned
 *   by `nextIntInRange` falls within the range.
 *
 * ## Usage:
 * This function is part of a test suite for extension functions on the `Random` class. It is crucial for ensuring
 * the reliability and correctness of the `nextIntInRange` function, especially in scenarios where precise control
 * over random integer generation within specific bounds is required.
 */
suspend fun FreeSpecContainerScope.`test next int in range`() {
    "when generating an int within a range" - {
        "should return a value within the range" {
            checkAll(
                Arb.random(),
                arbRange(Arb.int(), Arb.int()).filter { it.start < it.endInclusive }
            ) { random, range ->
                val value = random.nextIntInRange(range)
                value shouldBeInRange range
            }
        }
    }
}

/**
 * Tests the `indices` extension function for the `Random` class.
 *
 * This test function, organized using Kotest's FreeSpec style, orchestrates a series of subtests to validate
 * the functionality of the `indices` method under various scenarios. It ensures that the method behaves
 * correctly across different input conditions, including valid and invalid inputs.
 *
 * ## Test Structure:
 * - **Test Random Indices Invalid Probability**: Checks that the function throws an exception when provided
 *   with a pick probability outside the valid range of 0.0 to 1.0.
 * - **Test Random Indices Negative End Index**: Verifies that an exception is thrown when the end index is
 *   negative, which is an invalid input.
 * - **Test Random Indices Negative Start Index**: Ensures that an exception is thrown for a negative start
 *   index, another invalid input scenario.
 * - **Test Random Indices With Valid Inputs**: Confirms that the function returns a correct list of indices
 *   when provided with valid inputs, including a proper range and a valid pick probability.
 *
 * ## Usage:
 * This function serves as a comprehensive test suite for the `indices` method, covering edge cases and
 * typical usage scenarios. It's an essential part of ensuring the reliability and correctness of the
 * method in various contexts.
 */
suspend fun FreeSpecContainerScope.`test random indices`() {
    "when picking random indices" - {
        "by probability" - {
            `test random indices invalid probability`()
            `test random indices negative end index`()
            `test random indices negative start index`()
            `test random indices with valid inputs`()
        }

        "by size" - {
            `test random indices invalid size`()
            `test random indices with invalid end index`()
            `test random indices with invalid start index`()
            `test random indices when start index is greater than end index`()
            `test random indices with valid size`()
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices invalid size`() {
    "should throw an exception if the size is not positive" {
        checkAll(
            Arb.random(),
            Arb.nonPositiveInt(),
            Arb.int()
        ) { random, size, end ->
            shouldThrow<CompositeException> {
                random.indices(size, end)
            }.shouldHaveInfringement<IntConstraintException>("The size ($size) must be greater than 0.")
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices with invalid end index`() {
    "should throw an exception if the end index is negative" {
        checkAll(
            Arb.random(),
            Arb.negativeInt(),
            Arb.int(),
            Arb.double()
        ) { random, end, start, pickProbability ->
            shouldThrow<CompositeException> {
                random.indices(pickProbability, end, start)
            }.shouldHaveInfringement<IntConstraintException>("The end index ($end) must be greater than or equal to 0.")
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices with invalid start index`() {
    "should throw an exception if the start index is negative" {
        checkAll(
            Arb.random(),
            Arb.int(),
            Arb.negativeInt(),
            Arb.double()
        ) { random, end, start, pickProbability ->
            shouldThrow<CompositeException> {
                random.indices(pickProbability, end, start)
            }.shouldHaveInfringement<IntConstraintException>(
                "The start index ($start) must be greater than or equal to 0."
            )
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices when start index is greater than end index`() {
    "should throw an exception if the start index is greater than the end index" {
        checkAll(
            Arb.random(),
            arbOrderedPair(Arb.int(), Arb.int(), reversed = true),
            Arb.double()
        ) { random, (start, end), pickProbability ->
            shouldThrow<CompositeException> {
                random.indices(pickProbability, end, start)
            }.shouldHaveInfringement<IntConstraintException>(
                "The start index ($start) must be less than or equal to the end index ($end)."
            )
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices with valid size`() {
    "should return a list of indices" {
        checkAll(
            Arb.long().map { Random(it) to Random(it) },
            Arb.int(0..50),
            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() }
        ) { (r1, r2), size, pickProbability ->
            val indices = r1.indices(pickProbability, size)
            val expectedIndices = mutableListOf<Int>()
            for (i in 0 until size) {
                if (r2.nextDouble() < pickProbability) {
                    expectedIndices += i
                }
            }
            indices shouldBe expectedIndices
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices invalid probability`() {
    "should throw an exception if the pick probability is not in the range [0, 1]" {
        checkAll(
            Arb.random(),
            arbOrderedPair(Arb.int(), Arb.int(), reversed = true),
            Arb.double().filterNot { it in 0.0..1.0 }
        ) { random, (start, end), pickProbability ->
            shouldThrow<CompositeException> {
                random.indices(pickProbability, end, start)
            }.shouldHaveInfringement<DoubleConstraintException>(
                "The pick probability ($pickProbability) must be in the range [0, 1]"
            )
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices negative end index`() {
    "should throw an exception if the end index is negative" {
        checkAll(
            Arb.random(),
            Arb.negativeInt(),
            Arb.int(),
            Arb.double()
        ) { random, end, start, pickProbability ->
            shouldThrow<CompositeException> {
                random.indices(pickProbability, end, start)
            }.shouldHaveInfringement<IntConstraintException>("The end index ($end) must be greater than or equal to 0")
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices negative start index`() {
    "should throw an exception if the start index is negative" {
        checkAll(
            Arb.random(),
            Arb.int(),
            Arb.negativeInt(),
            Arb.double()
        ) { random, end, start, pickProbability ->
            shouldThrow<CompositeException> {
                random.indices(pickProbability, end, start)
            }.shouldHaveInfringement<IntConstraintException>(
                "The start index ($start) must be greater than or equal to 0"
            )
        }
    }
}

private suspend fun FreeSpecContainerScope.`test random indices with valid inputs`() {
    "should return a list of indices" {
        checkAll(
            Arb.long().map { Random(it) to Random(it) },
            arbOrderedPair(Arb.int(0..50), Arb.int(0..50), strict = true),
            Arb.double(0.0..1.0).filterNot { it.isNaN() || it.isInfinite() }
        ) { (r1, r2), (start, end), pickProbability ->
            val indices = r1.indices(pickProbability, end, start)
            val expectedIndices = mutableListOf<Int>()
            for (i in start until end) {
                if (r2.nextDouble() < pickProbability) {
                    expectedIndices += i
                }
            }
            indices shouldBe expectedIndices
        }
    }
}


suspend fun FreeSpecContainerScope.`test subset`() {
    "when picking a subset of elements" - {
        `test subsets with valid inputs`()
        `test subsets with exclusivity`()
        `test subsets maximum limit`()
        `test subsets exceptions`()
    }
}

private suspend fun FreeSpecContainerScope.`test subsets with valid inputs`() {
    "return a list of random subsets of the given size containing all elements" {
        checkAll(Arb.list(Arb.any(), 1..50).map {
            it to Arb.int(1..it.size).next()
        }, Arb.random()) { (elements, size), random ->
            val subsets = random.subsets(elements, size, false)
            assertSoftly {
                subsets.forEach { it.size shouldBe size }
                subsets.flatten() shouldContainAll elements
            }
        }
    }
}

private suspend fun FreeSpecContainerScope.`test subsets with exclusivity`() {
    "return random subsets of specified size with all unique elements if exclusivity is true." {
        checkAll(
            Arb.list(Arb.any(), 1..100).map { it to arbDivisor(it.size).next() },
            Arb.random()
        ) { (elements, size), random ->
            val subsets = random.subsets(elements, size, true)
            assertSoftly {
                subsets.forEach { it.size shouldBe size }
                subsets.flatten().size shouldBe elements.size
                subsets.flatten() shouldContainAll elements
            }
        }
    }
}

@OptIn(ExperimentalKotest::class)
private suspend fun FreeSpecContainerScope.`test subsets maximum limit`() {
    "return a list of at most the given number of subsets" {
        checkAll(
            PropTestConfig(iterations = 50),
            Arb.list(Arb.any(), 1..100).map { it to Arb.int(1..it.size).next() },
            Arb.positiveInt(),
            Arb.random()
        ) { (elements, size), limit, rng ->
            val subsets = rng.subsets(elements, size, false, limit)
            subsets.size shouldBeLessThanOrEqual limit
        }
    }
}

private suspend fun FreeSpecContainerScope.`test subsets exceptions`() {
    "throw an exception if" - {
        "the elements list is empty" {
            checkAll(
                Arb.positiveInt(), Arb.boolean(), Arb.random()
            ) { size, exclusivity, rng ->
                shouldThrow<CompositeException> {
                    rng.subsets(emptyList<Any>(), size, exclusivity)
                }.shouldHaveInfringement<CollectionConstraintException>("The input list must not be empty.")
            }
        }

        "the subsets size is non-positive" {
            checkAll(
                Arb.list(Arb.any(), 1..100), Arb.nonPositiveInt(), Arb.boolean(), Arb.random()
            ) { elements, size, exclusivity, rng ->
                val ex = shouldThrow<CompositeException> {
                    rng.subsets(elements, size, exclusivity)
                }
                with(ex.throwables.first()) {
                    shouldBeInstanceOf<IntConstraintException>()
                    message shouldBe "The subset size [$size] must be at least 1"
                }
            }
        }

        "the elements list's size is not valid when exclusivity is required" {
            checkAll(
                PropTestConfig(maxDiscardPercentage = 30),
                Arb.list(Arb.any(), 1..100)
                    .map { it to Arb.int(1..it.size).next() },
                Arb.random()
            ) { (elements, size), rng ->
                assume(elements.size % size != 0)
                shouldThrow<CompositeException> {
                    rng.subsets(elements, size, true)
                }.shouldHaveInfringement<ConstraintException>(
                    "Subset count [${elements.size}] must be a multiple of size [$size] for exclusivity."
                )
            }
        }

        "the limit is non-positive" {
            checkAll(
                Arb.list(Arb.any(), 1..100), Arb.nonPositiveInt(), Arb.boolean(), Arb.random()
            ) { elements, limit, exclusivity, rng ->
                val ex = shouldThrow<CompositeException> {
                    rng.subsets(elements, elements.size, exclusivity, limit)
                }
                with(ex.throwables.first()) {
                    shouldBeInstanceOf<IntConstraintException>()
                    message shouldBe "The limit [$limit] must be at least 1."
                }
            }
        }
    }
}