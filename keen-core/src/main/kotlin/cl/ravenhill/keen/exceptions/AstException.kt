package cl.ravenhill.keen.exceptions

import cl.ravenhill.jakt.Jakt
import cl.ravenhill.jakt.exceptions.ConstraintException

/**
 * Represents an exception for errors in the abstract syntax tree (AST) of a genetic programming context.
 *
 * `AstException` is a specialized exception class extending `ConstraintException`. It is specifically designed to be
 * thrown when encountering issues with the AST, such as invalid nodes or incompatible operations. This class plays a
 * crucial role in enforcing the correct structure of ASTs, ensuring that they adhere to defined constraints.
 *
 * ## Usage:
 * `AstException` is intended to be used within a [Jakt.constraints] block to enforce constraints on AST structures.
 * When an AST node violates a constraint, this exception is thrown, providing detailed error information.
 *
 * ### Example:
 * ```kotlin
 * constraints {
 *     "The arity of the function node must match the number of arguments"(::AstException) {
 *         functionNode.arity must BeEqual(argumentNodes.size)
 *     }
 * }
 * ```
 * In this example, the exception is used to assert that the arity of a function node matches the number of arguments
 * provided. If the condition is not met, an `AstException` with an appropriate message is raised according to the
 * specified [Jakt.shortCircuit] and [Jakt.skipChecks] flags.
 */
class AstException(message: String) : ConstraintException(message)
