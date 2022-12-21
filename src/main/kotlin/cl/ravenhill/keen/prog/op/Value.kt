package cl.ravenhill.keen.prog.op


sealed interface Value<T> : Fun<T> {
    class EphemeralConstant<T>(val factory: () -> T) : Value<T> {
        override val name: String = "ephemeral"
        override val arity: Int = 0
        override val function: (Array<out T>) -> T = { factory() }
    }
}