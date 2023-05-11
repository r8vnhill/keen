/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.EnforcementException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll


private fun Arb.Companion.condition() = arbitrary {
    element({depth: Int, maxHeight: Int -> depth < maxHeight}, {depth: Int, maxHeight: Int -> depth >= maxHeight})
}

class GeneratorsTest : FreeSpec({
    "Generating a [Tree] using the recursive method should" - {
        "throw an exception when" - {
            "there are no intermediate or leaf nodes." {
                checkAll(Arb.positiveInt(), Arb.positiveInt()) { depth, height ->
                    val ex = shouldThrow<EnforcementException> {
//                        Tree.generateRecursive(
//                            emptyList<TypedIntermediate<Any>>(),
//                            emptyList<TypedLeaf<Any>>(),
//                            depth,
//                            height,
//
//                        )
                    }
                }
            }
        }
    }
})