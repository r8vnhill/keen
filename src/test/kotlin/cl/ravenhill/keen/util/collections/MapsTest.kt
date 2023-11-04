/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.util.collections

import cl.ravenhill.keen.prog.Environment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class MapsTest : FreeSpec({
    "A [Map] of [String] and [Environment]" - {
        "can add a [Pair] of [String] and [Environment]" {
            checkAll<String> { name ->
                val map = mutableMapOf<String, Environment>()
                map += name to Environment(name)
                map.size shouldBe 1
                map[name] shouldBe Environment(name)
            }
        }

        "can add a [Pair] of [String] and [Environment] using the [plusAssign] operator" {
            checkAll<String> { name ->
                val map = mutableMapOf<String, Environment>()
                map += name to Environment(name)
                map.size shouldBe 1
                map[name] shouldBe Environment(name)
            }
        }
    }
})
