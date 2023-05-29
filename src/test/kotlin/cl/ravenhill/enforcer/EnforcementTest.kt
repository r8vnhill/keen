/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.enforcer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty


class EnforcementTest : FreeSpec({
    "The [Enforcement.Scope]" - {
        with(Enforcement.Scope()) {
            "has a list of" - {
                "[Result]s that is empty by default" {
                    results.shouldBeEmpty()
                }
                "[Failure]s that is empty by default" {
                    failures.shouldBeEmpty()
                }
            }
            "has a [StringScope] that" - {
                "can be created"
            }
        }
    }
})