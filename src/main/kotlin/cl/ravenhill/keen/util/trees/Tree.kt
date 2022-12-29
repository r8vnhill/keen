package cl.ravenhill.keen.util.trees

import cl.ravenhill.keen.util.Copyable


/**
 * Generic tree data structure.
 *
 * @param T The type of the value stored in the tree.
 */
interface Tree<T> {
    val depth: Int
}