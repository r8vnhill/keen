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

    /**
     * Returns an infix representation of the function.
     */
    fun toInfixString(): String = "(${left()} $name ${right()})"
}


/**
 * A function with double arguments.
 */
typealias DoubleFun = Fun<Double>

/**
 * An addition operation.
 *
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
object Add : DoubleFun, BinaryOperation<Double> {
    override val name: String = "+"
    override lateinit var left: Fun<Double>
    override lateinit var right: Fun<Double>
    override val function: (Array<out Double>) -> Double = { it[0] + it[1] }
    override fun toString() = toInfixString()
}

/**
 * A subtraction operation.
 *
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
object Sub : DoubleFun, BinaryOperation<Double> {
    override val name: String = "-"
    override lateinit var left: Fun<Double>
    override lateinit var right: Fun<Double>
    override val function: (Array<out Double>) -> Double = { it[0] - it[1] }
    override fun toString() = toInfixString()
}

/**
 * A multiplication operation.
 *
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
object Mul : DoubleFun, BinaryOperation<Double> {
    override val name: String = "*"
    override lateinit var left: Fun<Double>
    override lateinit var right: Fun<Double>
    override val function: (Array<out Double>) -> Double = { it[0] * it[1] }
    override fun toString() = toInfixString()
}