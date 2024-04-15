/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe


class MultiStringFormatTest : FreeSpec({

    "A MultiStringFormat object that" - {
        "has no string format logic" - {
            "should return the default string representation" {
                val multiStringFormat = object : MultiStringFormat {}
                multiStringFormat.toSimpleString() shouldBe multiStringFormat.toString()
                multiStringFormat.toDetailedString() shouldBe multiStringFormat.toString()
            }
        }
        "has string format logic" - {
            "should return a string representation according to the logic" {
                val multiStringFormat = object : MultiStringFormat {
                    override fun toSimpleString() = "simple"
                    override fun toDetailedString() = "detailed"
                }
                multiStringFormat.toSimpleString() shouldBe "simple"
                multiStringFormat.toDetailedString() shouldBe "detailed"
            }
        }
    }
})
