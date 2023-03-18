package cl.ravenhill.keen.prog


class ProgramNode<V, out R : Reduceable<V>>(val reduceable: R) {

    val arity: Int = reduceable.arity

    override fun toString() = reduceable.toString()
}