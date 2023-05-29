/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
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