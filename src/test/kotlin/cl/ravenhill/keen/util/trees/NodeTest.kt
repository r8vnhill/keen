package cl.ravenhill.keen.util.trees

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class NodeTest : FreeSpec({

    "A Node" - {
        "by default" - {
            "should have null contents" {
                checkAll(Arb.nonNegativeInt()) { arity ->
                    val node = object : Node<Any?> {
                        override val arity = arity
                    }
                    // Verifying that the contents are null by default
                    node.contents.shouldBeNull()
                }
            }
        }

        "when converted to a string" - {
            "should return the content's string representation for simple representation" {
                checkAll(Arb.nonNegativeInt(), Arb.int()) { arity, content ->
                    val node = object : Node<Int> {
                        override val arity = arity
                        override val contents = content
                    }
                    // Ensuring that the simple string representation matches the content's string
                    node.toSimpleString() shouldBe "$content"
                }
            }

            "should match the standard toString representation for detailed representation" {
                checkAll(Arb.nonNegativeInt(), Arb.int()) { arity, content ->
                    val node = object : Node<Int> {
                        override val arity = arity
                        override val contents = content
                    }
                    // Checking that detailed string representation is consistent with toString
                    node.toString() shouldBe node.toDetailedString()
                }
            }
        }
    }
})
