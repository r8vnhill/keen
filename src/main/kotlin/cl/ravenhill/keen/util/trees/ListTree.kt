package cl.ravenhill.keen.util.trees


/**
 * A tree data structure that uses a list to store its nodes.
 */
interface ListTree<T: Any> : Tree<T> {
    val children: List<T>
}