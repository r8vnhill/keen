package cl.ravenhill.keen.prog

import cl.ravenhill.keen.prog.AbstractBinaryOperation


/**
 * An addition operation.
 *
 * @constructor Creates a new addition function.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Add : AbstractBinaryOperation<Double, Double>("+", { a, b -> a + b }) {
    override fun toString() = "$left + $right"
}

/**
 * A subtraction operation.
 *
 * @constructor Creates a new subtraction function.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Sub : AbstractBinaryOperation<Double, Double>("-", { a, b -> a - b }) {
    override fun toString() = "$left - $right"
}

/**
 * A multiplication operation.
 *
 * @constructor Creates a new multiplication function.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Mul : AbstractBinaryOperation<Double, Double>("*", { a, b -> a * b }) {
    override fun toString() = "$left * $right"
}