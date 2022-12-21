package cl.ravenhill.keen.prog.op


class Variable<T>(
    override val name: String, val index: Int,
    override val arity: Int = 0,
    override val function: (Array<out T>) -> T = { it[0] }
) : Fun<T> {
    override fun toString() = name
}