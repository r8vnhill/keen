/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue


class CharacteristicsTest : FreeSpec({
    "A [Verifiable] should always verify to true by default" {
        val verifiable = object : Verifiable {}
        verifiable.verify().shouldBeTrue()
    }
})