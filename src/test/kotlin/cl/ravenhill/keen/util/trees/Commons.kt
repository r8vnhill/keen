/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

import cl.ravenhill.orderedPair
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list


// region : -== TREE NODES ==-
/**
 * Represents an intermediate node in a typed tree structure.
 * An intermediate node is a node that has a specific arity, which determines the number of child
 * nodes it can have.
 *
 * @param T the type of data contained in the node.
 * @property arity the arity of the intermediate node, which determines the number of child nodes
 * it can have.
 *
 * @constructor Creates a new instance of [TypedIntermediate] with the specified [arity].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
data class TypedIntermediate<T>(override val arity: Int) : Intermediate<T>

/**
 * Represents a leaf node in a typed tree structure.
 * A leaf node is a node that does not have any child nodes.
 *
 * @param T the type of data contained in the node.
 * @property value the value associated with the leaf node.
 *
 * @constructor Creates a new instance of [TypedLeaf] with the specified [value].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
data class TypedLeaf<T>(val value: T) : Leaf<T>
// endregion TREE NODES

/**
 * Represents a typed tree node that holds a reference to a node of type [Node] and a list of child
 * nodes of type [TypedTree].
 *
 * @param V the type of data contained in the node.
 * @property node the node object associated with this typed tree node.
 * @property children the list of child nodes of type [TypedTree].
 *
 * @param node the node object associated with this typed tree node.
 * @param children the list of child nodes of type [TypedTree]. It is an empty list by default.
 *
 * @property arity the arity of the typed tree node, which indicates the number of child nodes it
 * can have.
 * It is derived from the associated [node]'s arity.
 * @property value the value of the typed tree node, which is the associated [node].
 * @property nodes a list of all nodes in the typed tree, including the current node and its
 * descendants.
 *
 * @constructor Creates a new instance of [TypedTree] with the specified [node] and [children].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
data class TypedTree<V>(
    val node: Node<V>,
    override val children: List<TypedTree<V>> = emptyList()
) : Tree<Node<V>, TypedTree<V>> {
    /// Inherit documentation from Tree.
    override val arity: Int = node.arity

    /// Inherit documentation from Tree.
    override val value = node

    /// Inherit documentation from Tree.
    override fun createNode(value: Node<V>, children: List<TypedTree<V>>) =
        TypedTree(value, children)


    /// Inherit documentation from Tree.
    override val nodes: List<TypedTree<V>>
        get() = listOf(this) + children.flatMap { it.nodes }

    /// Inherit documentation from [Any].
    override fun toString() = "TypedTree($node, $children)"
}

// region : -== FACTORY FUNCTIONS ==-
/**
 * Creates a leaf node in the tree with the specified value.
 */
fun <T> leafFactory(value: Leaf<T>) = TypedTree(value, emptyList())

/**
 * Creates an intermediate node in the tree with the specified value and children.
 */
fun <T> intermediateFactory(value: Intermediate<T>, children: List<TypedTree<T>>) =
    TypedTree(value, children)
// endregion FACTORY FUNCTIONS

// region : -== GENERATORS ==-
/**
 * Creates an [Arb] instance that generates a leaf node in a tree with the specified `gen`
 * generator.
 */
fun <T> Arb.Companion.leaf(gen: Arb<T>) = arbitrary {
    val v = gen.bind()
    TypedLeaf(v)
}

/**
 * Creates an [Arb] instance that generates an intermediate node in a tree with the specified
 * [arity] generator.
 * The [arity] generator determines the number of children the intermediate node will have.
 *
 * @param arity The generator to use for generating the arity (number of children) of the
 * intermediate node.
 * @return A new instance of [Arb] representing a generator for an intermediate node.
 * @throws IllegalArgumentException if the generated arity is not positive.
 */
fun <T> Arb.Companion.intermediate(arity: Arb<Int> = Arb.int(1..100)) =
    arbitrary {
        val a = arity.bind()
        require(a > 0)
        TypedIntermediate<T>(a)
    }

private fun <T> Arb.Companion.tree(gen: Arb<T>, maxDepth: IntRange = 1..20) = arbitrary {
    val (lo, hi) = orderedPair(int(maxDepth), int(maxDepth)).bind()
    Tree.generate(
        leafs = list(leaf(gen)).bind(),
        intermediates = list(intermediate<T>()).bind(),
        min = lo,
        max = hi,
        condition = { _, _ -> true },
        leafFactory = { value -> leafFactory(value) }
    ) { value, children -> intermediateFactory(value, children) }
}
// endregion GENERATORS