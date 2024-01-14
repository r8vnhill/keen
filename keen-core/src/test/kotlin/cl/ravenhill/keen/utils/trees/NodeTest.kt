/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils.trees

import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.arb.node
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

@OptIn(ExperimentalKeen::class)
class NodeTest : FreeSpec({

    "A Node" - {
        "should have a content property that" - {
            "is null by default" {
                checkAll(Arb.node<Any?>()) { node ->
                    node.contents.shouldBeNull()
                }
            }
        }

        "can be converted to a simple string" - {
            "using the contents property" {
                checkAll(Arb.node<Int>()) { node ->
                    node.toSimpleString() shouldBe "${node.contents}"
                }
            }
        }
    }
})
