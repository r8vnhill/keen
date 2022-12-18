package cl.ravenhill.keen.prog


/**
 * A binary operation.
 *
 * @param T The type of the operands.
 * @param R The type of the reduced value.
 * @property left The left operand.
 * @property right The right operand.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface BinaryOperation<T, R> : Reduceable<R> {
    var left: Reduceable<T>
    var right: Reduceable<T>
}


/**
 * Abstract class for binary operations.

 * @param T The type of the operands.
 * @param R The type of the reduced value.
 * @property name The name of the operation.
 * @property function The function to reduce the operation.
 * @property left The left operand.
 * @property right The right operand.
 * @constructor Creates a new binary operation.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
abstract class AbstractBinaryOperation<T, R>(
    private val name: String,
    private val function: (T, T) -> R
) : BinaryOperation<T, R> {
    override lateinit var left: Reduceable<T>
    override lateinit var right: Reduceable<T>

    override fun reduce() = function(left.reduce(), right.reduce())

    override fun toString() = "($left $name $right)"
}