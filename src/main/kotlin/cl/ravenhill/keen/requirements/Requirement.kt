package cl.ravenhill.keen.requirements


/**
 * A constraint that can be applied to a value of type [T].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Requirement<T> {

    /**
     * Checks if the given value fulfills the constraint.
     */
    fun validate(value: T): Result<T>
}
