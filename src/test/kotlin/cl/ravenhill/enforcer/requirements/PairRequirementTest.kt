/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.enforcer.requirements

import cl.ravenhill.enforcer.PairRequirementException
import cl.ravenhill.enforcer.requirements.PairRequirement.BeStrictlyOrdered
import cl.ravenhill.orderedPair
import cl.ravenhill.unfulfilledConstraint
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class PairRequirementTest : FreeSpec({
    "Generating an exception should return a [PairRequirementException]" {
        checkAll(Arb.pairRequirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<PairRequirementException>()
                message shouldBe unfulfilledConstraint(description)
            }
        }
    }

    "A [BeStrictlyOrdered] requirement" - {
        "can be converted to a [String]" {
            BeStrictlyOrdered<Int>() shouldBe "BeStrictlyOrdered<Int>"
        }

        "when _validating_ that a pair is strictly ordered should return" - {
            "[true] if the pair is strictly ordered" {
                checkAll(Arb.orderedPair(Arb.int(), strict = true, reverted = false)) { pair ->
                    BeStrictlyOrdered<Int>().validator(pair).shouldBeTrue()
                }
            }

            "[false] if the pair is not strictly ordered" {
                checkAll(Arb.orderedPair(Arb.int(), strict = false, reverted = true)) { pair ->
                    BeStrictlyOrdered<Int>().validator(pair).shouldBeFalse()
                }
            }
        }
    }
})

private fun Arb.Companion.pairRequirement() = arbitrary {
    element(
        BeStrictlyOrdered<Int>()
    ).bind()
}

private fun Arb.Companion.beStrictlyOrderedData(
    gen: Arb<Int> = Arb.int(),
    strictlyOrdered: Boolean = true
) = arbitrary {
    BeStrictlyOrderedData(
        orderedPair(
            gen,
            strict = strictlyOrdered,
            reverted = !strictlyOrdered
        ).bind()
    )
}

data class BeStrictlyOrderedData<T : Comparable<T>>(
    val test: Pair<T, T>,
    val requirement: PairRequirement<T, T> = BeStrictlyOrdered()
)
