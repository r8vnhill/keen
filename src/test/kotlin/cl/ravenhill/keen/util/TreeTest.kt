/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util

import cl.ravenhill.keen.util.trees.Tree
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int

private class AnyTree(
    override val value: Any,
    override val children: List<AnyTree>,
    override val nodes: List<AnyTree>,
    override val arity: Int
) : Tree<Any, AnyTree> {
    override fun createNode(value: Any, children: List<AnyTree>) =
        AnyTree(value, children, children.flatMap { it.nodes }, children.size)
}

private fun Arb.Companion.tree(maxDepth: IntRange = 1..100) = arbitrary {
    val depth = int(maxDepth)

}

class TreeTest : FreeSpec({

})