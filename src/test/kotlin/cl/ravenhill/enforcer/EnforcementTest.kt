/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.enforcer

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class EnforcementTest : FreeSpec({
    beforeEach {
        Enforcement.skipChecks = false
    }

    "The skip checks flag" - {
        "has a default value of false" {
            Enforcement.skipChecks shouldBe false
        }

        "can be set to true" {
            Enforcement.skipChecks shouldBe false
            Enforcement.skipChecks = true
            Enforcement.skipChecks shouldBe true
        }
    }

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