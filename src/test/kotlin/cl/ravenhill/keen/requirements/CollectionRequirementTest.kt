/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.CollectionRequirementException
import cl.ravenhill.keen.any
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


private fun Arb.Companion.beEmpty() = arbitrary { CollectionRequirement.BeEmpty }

private fun Arb.Companion.requirement() = arbitrary {
    choice(beEmpty()).bind()
}

class CollectionRequirementTest : FreeSpec({
    "Generating an exception should return a CollectionRequirementException" {
        checkAll(Arb.requirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<CollectionRequirementException>()
                message shouldBe "Unfulfilled constraint: $description"
            }
        }
    }

    "Validating that a collection is empty should" - {
        "return a success if the collection is empty" {
            checkAll(Arb.beEmpty(), Arb.string()) { requirement, description ->
                with(requirement.validate(emptyList<Any>(), description)) {
                    shouldBeSuccess()
                    getOrNull() shouldBe emptyList<Any>()
                }
            }
        }

        "return a failure if the collection is not empty" {
            checkAll(
                Arb.beEmpty(),
                Arb.string(),
                Arb.list(Arb.any(), 1..100)
            ) { requirement, description, value ->
                with(requirement.validate(value, description)) {
                    shouldBeFailure()
                    exceptionOrNull() shouldBe requirement.generateException(description)
                }
            }
        }
    }
})