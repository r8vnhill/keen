package cl.ravenhill.keen.prog.op


/**
 * Function that takes a list of values and returns a single value.
 *
 * @param T The type of the input values.
 * @property name The name of the function.
 * @property arity The arity of the function.
 */
interface Fun<T> {
    val name: String
    val arity: Int

    /**
     * Reduces the function to a single value.
     *
     * @param args The inputs to the function.
     * @return The reduced value.
     */
    operator fun invoke(vararg args: T): T

    /**
     * Return this function, or a new instance from the same type.
     */
    operator fun invoke(): Fun<T>
}