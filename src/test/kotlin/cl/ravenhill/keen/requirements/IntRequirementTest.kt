/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.IntRequirementException
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.unfulfilledConstraint
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Generates an arbitrary instance of [IntRequirement].
 *
 * @receiver The `Arb.Companion` object.
 * @return An [Arb] instance that generates [IntRequirement]s.
 */
private fun Arb.Companion.intRequirement() = arbitrary {
    choice(constant(BePositive)).bind()
}

class IntRequirementTest : FreeSpec({
    "Generating an exception should return an [IntRequirementException]" {
        checkAll(Arb.intRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<IntRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "A [BePositive] requirement" - {
        "can be converted to a [String]" {
            BePositive.toString() shouldBe "BePositive"
        }

        "when validating that an [Int] is positive should return" - {
            "[true] if it is positive" {
                checkAll(Arb.positiveInt()) { value ->
                    BePositive.validator(value).shouldBeTrue()
                }
            }

            "[false] if it is not positive" {
                checkAll(Arb.nonPositiveInt()) { value ->
                    BePositive.validator(value).shouldBeFalse()
                }
            }
        }


    }
})