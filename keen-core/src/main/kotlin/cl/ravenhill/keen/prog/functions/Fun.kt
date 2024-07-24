package cl.ravenhill.keen.prog.functions

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.constraints.ints.BeNegative
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.exceptions.AstException
import cl.ravenhill.keen.prog.Environment
import cl.ravenhill.keen.prog.Reducible
import cl.ravenhill.keen.utils.trees.Intermediate

/**
 * Represents a function-like structure within a computational model, encapsulating a named function with a defined
 * arity and a specific body. As an open class, it can be extended to create various function-like entities. This class
 * combines the functionalities of both `Reducible` and `Intermediate` interfaces, making it a versatile component in
 * computational structures. It is marked as experimental in the Keen library.
 *
 * ## Constraints:
 * - During initialization, it ensures that the `arity` of the function is non-negative.
 *
 * ## Usage:
 * This class is suitable for defining custom functions within computational models, especially where functions are
 * first-class citizens, such as in genetic programming. It can also be used to create custom function-like structures
 * that are capable of reducing their arguments to produce an outcome.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * val sum = Fun<Int>("sum", 2) { args -> args.sum() }
 * val result = sum(Environment("env1"), listOf(1, 2))
 * // result is 3
 * ```
 *
 * @param T the type of the values that the function deals with.
 * @property name  The name of the function, useful for identification or debugging purposes.
 * @property arity The number of arguments that the function accepts. It must be a non-negative value.
 * @property body A lambda expression that defines the behavior or computation performed by the function when invoked.
 * @constructor Creates a new function with the given name, arity, and body.
 * @see Reducible for reduction capabilities.
 * @see Intermediate for its role as an intermediate node.
 * @see ExperimentalKeen for the experimental status of this class.
 */
@ExperimentalKeen
open class Fun<T>(val name: String, override val arity: Int, val body: (List<T>) -> T) : Reducible<T>,
        Intermediate<Reducible<T>> {

    init {
        constraints {
            "The arity ($arity) must be greater than or equal to 0"(::AstException) { arity mustNot BeNegative }
        }
    }

    /**
     * Executes the function represented by this `Fun` instance. This method is the primary mechanism for invoking the
     * function, using the provided environment and arguments. It ensures that the function is called with the correct
     * number of arguments as defined by its arity.
     *
     * ## Functionality:
     * - Takes an [Environment]<[T]> and a list of arguments (`List<T>`) as input.
     * - Validates that the number of arguments provided matches the function's arity, ensuring the function receives
     *   the correct number of inputs.
     * - Executes the function's body with the given arguments and returns the result.
     *
     * ## Constraints:
     * - The method enforces that the size of the argument list must match the function's arity. This constraint
     *   ensures that the function is invoked with the appropriate number of arguments, aligning with its definition.
     *
     * ## Usage:
     * Implement this method to define the specific logic of how the function should operate based on the provided
     * arguments. This method is crucial for executing the function's core behavior in a controlled and expected manner.
     *
     * ### Example:
     * ```
     * class MyFunction : Fun<String>("myFunction", 2) { args ->
     *     // Function logic that processes the arguments
     *     args.joinToString(separator = "-")
     * }
     *
     * val myFunction = MyFunction()
     * val result = myFunction(Environment("env1"), listOf("Hello", "World"))
     * // result will be "Hello-World"
     * ```
     *
     * @param environment The `Environment<T>` in which the function operates.
     * @param args A list of arguments of type `T` used to invoke the function.
     * @return The result of the function after processing the arguments.
     * @throws CompositeException containing a [CollectionConstraintException] if the number of arguments does not match
     *   the function's arity.
     * @throws CollectionConstraintException if the number of arguments does not match the function's arity.
     */
    override fun invoke(environment: Environment<T>, args: List<T>): T {
        constraints {
            "The number of arguments [${args.size}] must be equal to the arity [$arity]" {
                args must HaveSize(arity)
            }
        }
        return body(args)
    }
}
