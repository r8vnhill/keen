/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue


/**
 * Test suite for verifying characteristics of [Verifiable] instances.
 *
 * ## Examples
 * ### Example 1: Default verification
 * ```
 * val verifiable = object : Verifiable {}
 * verifiable.verify().shouldBeTrue()
 * ```
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Commons : FreeSpec({
    "A [Verifiable] should always verify to true by default" {
        val verifiable = object : Verifiable {}
        verifiable.verify().shouldBeTrue()
    }
})
