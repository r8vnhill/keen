package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.prog.Reducible
import cl.ravenhill.keen.utils.trees.Leaf

/**
 * Represents a terminal element in computational structures, combining the functionalities of both `Reducible` and
 * `Leaf` interfaces.
 * As a terminal element, it acts as a leaf node in hierarchical structures and can be reduced (evaluated) based on
 * specific environmental parameters. Marked as experimental in the Keen library, this interface is intended for use
 * in advanced computational models and may undergo changes in future versions.
 *
 * ## Usage:
 * `Terminal` interfaces are particularly useful in scenarios such as expression trees, decision-making structures, or
 * other computational models where the end nodes (terminals) carry out specific evaluations or actions. They are
 * crucial in scenarios where a computation or decision reaches its final stage.
 *
 * ### Example Implementation:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * class MyTerminal : Terminal<String> {
 *     override fun create(): Terminal<String> {
 *         // Logic to create and initialize a new instance of MyTerminal
 *     }
 *
 *     // Implement other required members from Reducible and Leaf interfaces
 * }
 *
 * // Creating a new instance of MyTerminal
 * val terminal = MyTerminal().create()
 * ```
 *
 * @param T the type of the values used in the reduction process in the terminal.
 * @see ExperimentalKeen for the experimental status of this interface.
 */
@ExperimentalKeen
interface Terminal<T> : Reducible<T>, Leaf<Reducible<T>> {

    /**
     * Defines a method to create and return a new instance of the `Terminal` type. This method is essential for
     * instantiating terminals within computational models, allowing for the creation of new terminal elements with
     * potentially specific initialization or configuration.
     *
     * ## Functionality:
     * - The `create` method is responsible for generating a new instance of a `Terminal<T>`.
     * - It should be implemented to include any initialization logic necessary for the specific type of terminal.
     * - This method provides flexibility in creating various instances of terminals, each potentially configured or
     *   initialized differently based on the requirements of the computational model.
     *
     * ## Usage:
     * Implement this method in classes that extend the `Terminal` interface to define how new instances of the terminal
     * are created. It can be used to instantiate terminals with specific initial states or configurations required for
     * the computational process.
     *
     * ### Example Implementation:
     * ```
     * @OptIn(ExperimentalKeen::class)
     * class MyTerminal : Terminal<String> {
     *     override fun create(): Terminal<String> {
     *         // Implementation to create a new MyTerminal instance
     *         return MyTerminal() // Or with specific initial configuration
     *     }
     * }
     *
     * // Using the create method to instantiate a new terminal
     * val newTerminalInstance = MyTerminal().create()
     * ```
     *
     * @return A new instance of the `Terminal<T>` type.
     * @see Terminal for details on the terminal interface.
     */
    fun create(): Terminal<T>
}
