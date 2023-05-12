/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work.
 * If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

/***************************************************************************************************
 * This file defines three interfaces: Node<T>, Intermediate<T>, and Leaf<T>, representing
 * different types of nodes in a tree structure.
 * The Node<T> interface represents a general node in the tree and specifies the arity property,
 * which indicates the number of child nodes the node can have.
 * The Intermediate<T> interface extends Node<T> and represents an intermediate node with a
 * specific arity.
 * It also inherits the arity property from Node<T>.
 * The Leaf<T> interface extends Node<T> and represents a leaf node, which has no child nodes and
 * always has an arity of zero.
 * Each interface includes the type parameter T to indicate the type of data contained in the nodes.
 **************************************************************************************************/

/**
 * Represents a node in a tree structure.
 *
 * @param T the type of data contained in the node.
 *
 * @property arity the arity of the node, which determines the number of child nodes it can have.
 * The restriction on the positive value of the arity should be dealt with by the implementers.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Node<T> {
    val arity: Int
}

/**
 * Represents an intermediate node in a tree structure.
 * An intermediate node is a node that has a specific arity, which determines the number of child
 * nodes it can have.
 *
 * @param T the type of data contained in the node.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Intermediate<T> : Node<T> {
    /// Documentation inherited from [Node]
    override val arity: Int
}

/**
 * Represents a leaf node in a tree structure.
 * A leaf node is a node that does not have any child nodes.
 *
 * @param T the type of data contained in the node.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface Leaf<T> : Node<T> {
    /**
     * The arity of the leaf node, which is always zero.
     */
    override val arity: Int get() = 0
}
