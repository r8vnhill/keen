/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.logging

import cl.ravenhill.kuro.Level
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldNotBe


/**
 * Asserts that a [Level] instance should not be equal to any of the levels in the given
 * list.
 *
 * @param levels the list of levels to compare against.
 */
private infix fun <T> T.shouldNotBeAnyOf(levels: List<T>) = assertSoftly {
    levels.forEach {
        this shouldNotBe it
    }
}

/**
 * Asserts that a value is greater than all other values in a list of comparable values.
 *
 * @param others a list of comparable values to compare against the receiver.
 * @throws AssertionError if the receiver is not greater than all other values.
 */
private infix fun <T : Comparable<T>> T.shouldBeGreaterThanAll(others: List<T>) = assertSoftly {
    others.forEach {
        this shouldBeGreaterThan it
    }
}

/**
 * Asserts that a value is less than all other values in a list of comparable values.
 *
 * @param others a list of comparable values to compare against the receiver.
 * @throws AssertionError if the receiver is not less than all other values.
 */
private infix fun <T : Comparable<T>> T.shouldBeLessThanAll(others: List<T>) = assertSoftly {
    others.forEach {
        this shouldBeLessThan it
    }
}