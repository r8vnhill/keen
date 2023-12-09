package cl.ravenhill.keen.prog

import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.mixins.SelfReferential
import cl.ravenhill.keen.utils.trees.Node

/**
 * Represents a reducible structure in a computation or data processing context. As an extension of the `Node`
 * interface, `Reducible` can be used to create complex nested structures that can be reduced (evaluated) based on
 * specific environmental parameters and arguments, like variables and other values.
 *
 * ## Usage:
 * The `Reducible` interface is useful in scenarios where structures need to be evaluated or processed in the context
 * of an environment, particularly in computational frameworks where parameters and external conditions affect the
 * outcome of such evaluations.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * class MyReducibleFunction : Reducible<String> {
 *     override fun invoke(environment: Environment<String>, args: List<String>): String {
 *         // Implementation of reduction logic
 *     }
 * }
 *
 * val reducible: Reducible<String> = MyReducibleFunction()
 * val result = reducible(Environment("env1"), listOf("arg1", "arg2"))
 * ```
 *
 * @param T the type of the values used in the reduction process.
 * @property contents Self-reference to the `Reducible` instance.
 * @see ExperimentalKeen for information on the experimental status of this interface.
 */
@ExperimentalKeen
interface Reducible<T> : Node<Reducible<T>>, SelfReferential<Reducible<T>> {
    override val contents: Reducible<T> get() = this

    /**
     * Operates as the primary function of the `Reducible` interface, defining how a `Reducible` instance is evaluated
     * or "reduced" in a given environment with a list of arguments. This function is central to the functionality of
     * `Reducible` objects, specifying their behavior and computation logic.
     *
     * ## Functionality:
     * - Takes an `Environment<T>` and a list of arguments (`List<T>`) as input.
     * - The environment provides the context in which the `Reducible` instance operates, potentially influencing its
     *   behavior.
     * - The list of arguments is used in the computation or processing performed by the `Reducible` instance.
     * - Returns a value of type `T` as the result of the reduction process.
     *
     * ## Usage:
     * Implement this function to define the specific logic of how a `Reducible` instance should process or compute
     * based on the provided environment and arguments. This is where the core behavior of the `Reducible` structure is
     * specified.
     *
     * ### Example:
     * ```
     * class MyReducibleFunction : Reducible<String> {
     *     override fun invoke(environment: Environment<String>, args: List<String>): String {
     *         // Define the logic of reduction here, possibly using the environment and args
     *         return "Computed result based on args"
     *     }
     * }
     *
     * val reducibleFunction = MyReducibleFunction()
     * val result = reducibleFunction(Environment("env1"), listOf("arg1", "arg2"))
     * // result holds the value returned by the reduction logic
     * ```
     *
     * @param environment The `Environment<T>` in which the `Reducible` instance operates.
     * @param args A list of arguments of type `T` used in the reduction process.
     * @return The result of the reduction process, of type `T`.
     */
    operator fun invoke(environment: Environment<T>, args: List<T>): T

    /**
     * Overloads the `invoke` operator for the `Reducible` interface to accept a variable number of arguments. This
     * function provides a more flexible and convenient way of passing arguments to a `Reducible` instance for
     * evaluation or computation within a given environment.
     *
     * ## Functionality:
     * - Takes an [Environment]<[T]> and a variable number of arguments (`vararg args: T`) as input.
     * - Converts the variable arguments into a list and delegates the processing to the primary `invoke` function
     *   that accepts a list of arguments.
     * - Returns the result of the reduction process, which is computed by the primary `invoke` function.
     *
     * ## Usage:
     * This operator function allows for a more concise and flexible invocation of `Reducible` instances, especially
     * when the number of arguments is not fixed or known in advance. It enhances usability by allowing arguments to be
     * passed directly, without explicitly creating a list.
     *
     * ### Example:
     * ```
     * class MyReducibleFunction : Reducible<String> {
     *     override fun invoke(environment: Environment<String>, args: List<String>): String {
     *         // Reduction logic implementation
     *     }
     * }
     *
     * val reducibleFunction = MyReducibleFunction()
     * val result = reducibleFunction(Environment("env1"), "arg1", "arg2", "arg3")
     * // result holds the value returned by the reduction logic
     * ```
     *
     * @param environment The `Environment<T>` in which the `Reducible` instance operates.
     * @param args A variable number of arguments of type `T` used in the reduction process.
     * @return The result of the reduction process, of type `T`.
     */
    operator fun <T> Reducible<T>.invoke(environment: Environment<T>, vararg args: T): T =
        invoke(environment, args.toList())
}
