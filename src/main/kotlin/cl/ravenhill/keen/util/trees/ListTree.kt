package cl.ravenhill.keen.util.trees


/**
 * A tree data structure that uses an array to store its nodes.
 */
interface ListTree<T: Any> : Tree<T> {
    var children: List<T>
}