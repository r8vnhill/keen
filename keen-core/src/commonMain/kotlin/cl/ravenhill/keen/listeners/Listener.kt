/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

/**
 * A read-only listener interface for monitoring and displaying information.
 *
 * The `Listener` interface defines a minimal contract for listeners that are intended to observe and potentially
 * display information in an application. It provides a method to display the listener's state and a method to create a
 * copy of the listener. This interface is particularly useful in scenarios where listeners are used to monitor events
 * or states but should not be modified directly.
 *
 * ## Recommendations:
 * - The `copy` method should return a new instance or a deep copy of the listener to ensure that changes to the copy do
 *   not affect the original listener.
 */
interface Listener {

    /**
     * Displays the listener's state.
     *
     * This method outputs the result of the `toString` method to the console. Implementing classes should override
     * this method to provide a custom display format for the listener's state. This could be through a formatted
     * string, a graphical representation, or any other suitable output.
     */
    fun display() = println(toString())

    /**
     * Creates and returns a copy of the listener.
     *
     * This method is intended to return a copy of the listener, which can be used in contexts where the listener needs
     * to be duplicated without modifying the original instance. The implementation of this method should ensure that
     * the copied listener is independent of the original.
     *
     * @return A new instance of the listener or a deep copy, depending on the implementation.
     */
    fun copy(): Listener
}
