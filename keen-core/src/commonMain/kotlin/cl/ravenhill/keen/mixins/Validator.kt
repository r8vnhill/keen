/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * Marker interface for performing validations on constructor parameters using delegation.
 *
 * The `Validator` interface is designed to serve as a marker for classes that encapsulate validation logic,
 * particularly when using delegation to perform validations on constructor parameters. By implementing this interface,
 * classes signal that they are responsible for validating the parameters passed to constructors or other initialization
 * routines. This allows for clean separation of concerns, where validation logic is kept distinct from the core
 * functionality of a class.
 *
 * ## Usage:
 * The `Validator` interface is intended to be used in conjunction with Kotlin's delegation feature. Instead of
 * implementing validation logic directly within a class, you can delegate the responsibility to a separate `Validator`
 * object that performs the necessary checks. This approach promotes code modularity and reusability.
 *
 * ### Example: Using a Validator with Delegation
 * ```kotlin
 * class MyClass(
 *     param1: Int,
 *     param2: String,
 * ) : Validator by MyValidator(param1, param2)
 *
 * class MyValidator(private val param1: Int, private val param2: String) : Validator {
 *     init {
 *         // Perform validation logic here
 *     }
 * }
 *
 * val myClass = MyClass(10, "Hello")
 * ```
 *
 * In this example, the `MyValidator` class implements the `Validator` interface and is used to validate the
 * constructor parameters of `MyClass` through delegation. The validation logic is executed during the initialization
 * of `MyValidator`, ensuring that the parameters meet the necessary requirements before `MyClass` is fully constructed.
 *
 * ## Notes:
 * - The `Validator` interface itself does not define any methods; it serves as a marker to indicate validation
 *   responsibility.
 * - Delegation allows for validation logic to be separated from the core class, enhancing modularity and making the
 *   code more maintainable.
 */
interface Validator
