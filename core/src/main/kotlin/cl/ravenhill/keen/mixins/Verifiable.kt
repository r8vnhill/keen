/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins


/**
 * A mixin interface providing a verification capability.
 *
 * Implementing this interface allows an object to have a `verify` method, which
 * can be used to perform custom verification logic. By default, the `verify` method
 * simply returns `true`, indicating successful verification.
 *
 * Classes implementing this interface can override the `verify` method to introduce
 * specific verification logic suitable for their context.
 *
 * ## Usage:
 * This interface is useful in scenarios where objects need to be verified before
 * proceeding with further processing. For instance, it can be used to ensure that
 * an object is in a valid state or meets certain criteria.
 *
 * ### Example:
 * Implementing the `Verifiable` interface in a class:
 * ```kotlin
 * class MyData : Verifiable {
 *     override fun verify(): Boolean {
 *         // Custom verification logic
 *         return true // Return true if verification is successful
 *     }
 * }
 *
 * val myData = MyData()
 * if (myData.verify()) {
 *     // Proceed if verification is successful
 * }
 * ```
 *
 * In this example, `MyData` class implements the `Verifiable` interface and provides
 * a custom implementation of the `verify` method. This method can then be used to
 * ensure that `myData` is in a state that is safe for further operations.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
interface Verifiable {
    /**
     * Performs verification and returns the result.
     *
     * @return A boolean indicating the success of the verification. By default, it returns `true`.
     */
    fun verify(): Boolean = true
}
