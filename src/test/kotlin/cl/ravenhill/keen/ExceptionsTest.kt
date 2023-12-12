/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.keen.arbs.uniqueStrings
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ExceptionsTest : FreeSpec({
    "A KeenException" - {
        "can be created with a prefix and a message" {
            checkAll<String, String> { prefix, message ->
                val exception = KeenException(prefix) { message }
                exception.message shouldBe "$prefix $message"
            }
        }
    }

    "An InvalidStateException" - {
        "can be created with a state and a message" {
            checkAll<String, String> { state, message ->
                val exception = InvalidStateException(state) { message }
                exception.message shouldBe "Invalid state ($state): $message"
            }
        }

        "equality" - {
            "is true for another InvalidStateException with the same state and message" {
                checkAll<String, String> { state, message ->
                    val exception = InvalidStateException(state) { message }
                    val other = InvalidStateException(state) { message }
                    exception shouldBe other
                }
            }

            "is false for another InvalidStateException with a different state" {
                checkAll(Arb.uniqueStrings(2..2), Arb.string()) { states, message ->
                    val exception = InvalidStateException(states[0]) { message }
                    val other = InvalidStateException(states[1]) { message }
                    exception shouldNotBe other
                }
            }

            "is false for another InvalidStateException with a different message" {
                checkAll(Arb.string(), Arb.uniqueStrings(2..2)) { state, messages ->
                    val exception = InvalidStateException(state) { messages[0] }
                    val other = InvalidStateException(state) { messages[1] }
                    exception shouldNotBe other
                }
            }
        }

        "hashing" - {
            "is the same for another InvalidStateException with the same state and message" {
                checkAll<String, String> { state, message ->
                    val exception = InvalidStateException(state) { message }
                    val other = InvalidStateException(state) { message }
                    exception shouldHaveSameHashCodeAs other
                }
            }

            "is different for another InvalidStateException with a different state" {
                checkAll(Arb.uniqueStrings(2..2), Arb.string()) { states, message ->
                    val exception = InvalidStateException(states[0]) { message }
                    val other = InvalidStateException(states[1]) { message }
                    exception shouldNotHaveSameHashCodeAs other
                }
            }

            "is different for another InvalidStateException with a different message" {
                checkAll(Arb.string(), Arb.uniqueStrings(2..2)) { state, messages ->
                    val exception = InvalidStateException(state) { messages[0] }
                    val other = InvalidStateException(state) { messages[1] }
                    exception shouldNotHaveSameHashCodeAs other
                }
            }
        }
    }

    "An Illegal Operation Exception " - {
        "can be created with a message" {
            checkAll<String> { message ->
                val exception = IllegalOperationException { message }
                exception.message shouldBe "Illegal operation: $message"
            }
        }
    }
})