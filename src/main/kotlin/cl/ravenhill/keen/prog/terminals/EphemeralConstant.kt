package cl.ravenhill.keen.prog.terminals


class EphemeralConstant<T>(val generator: () -> T) : Terminal<T> {
    override val arity: Int = 0
    override fun invoke(args: Array<out T>) = generator()
}