package cl.ravenhill.keen.prog.op

/**
 * A function with two arguments.
 *
 * @param R The type of the reduced value.
 * @property left The left argument.
 * @property right The right argument.
 * @property arity The arity of the function (2).
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface BinaryOperation<R> : Fun<R> {
    var left: Fun<R>
    var right: Fun<R>
    override val arity: Int
        get() = 2
    fun toInfixString(): String = "(${left()} $name ${right()})"
}


typealias DoubleFun = Fun<Double>

object MathOps {
    object Add : DoubleFun, BinaryOperation<Double> {
        override val name: String = "+"
        override lateinit var left: Fun<Double>
        override lateinit var right: Fun<Double>
        override val function: (Array<out Double>) -> Double = { it[0] + it[1] }
        override fun toString() = toInfixString()
    }
}