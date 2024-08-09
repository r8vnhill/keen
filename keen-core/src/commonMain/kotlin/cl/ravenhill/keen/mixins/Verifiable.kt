/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins


/**
 * Interface representing a verifiable component in the Keen evolutionary computation framework.
 *
 * The `Verifiable` interface defines a contract for objects that can be verified for correctness or validity.
 * Implementing classes can override the `verify` method to provide custom verification logic.
 *
 * ## Usage:
 * This interface is useful for ensuring that components within the evolutionary algorithm meet certain criteria or
 * constraints before being used. The default implementation of `verify` returns `true`, indicating that the component
 * is valid. Implementing classes can override this method to include specific verification logic.
 *
 * ### Example 1: Custom Verification
 * ```kotlin
 * class MyFeature(
 *    val value: Int
 * ) : Feature<Int, MyFeature>, Verifiable {
 *     override fun duplicateWithValue(value: Int) = MyFeature(value)
 *     override fun verify() = value > 0
 * }
 * ```
 */
interface Verifiable {
    /**
     * Verifies the correctness or validity of the object.
     *
     * @return true if the object is verified, false otherwise.
     */
    fun verify(): Boolean = true
}
