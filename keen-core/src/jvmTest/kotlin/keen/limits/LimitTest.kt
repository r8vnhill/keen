package keen.limits

import io.kotest.assertions.fail
import io.kotest.assertions.failure
import io.kotest.core.spec.style.FreeSpec

class LimitTest : FreeSpec({
    "A Limit" - {
        "when invoked" - {
            "should return false if the predicate is false" - {
                "for a limit with a predicate that always returns false" {
                    fail("Not implemented")
                }

                "for a limit with a predicate that returns false for a specific state" {
                    fail("Not implemented")
                }
            }

            "should return true if the predicate is true" - {
                "for a limit with a predicate that always returns true" {
                    fail("Not implemented")
                }

                "for a limit with a predicate that returns true for a specific state" {
                    fail("Not implemented")
                }
            }
        }
    }
})
