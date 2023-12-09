package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen

/**
 * Represents an environment with a unique identifier, capable of storing and managing memory elements.
 * This class is part of the Keen library and is currently marked as experimental.
 *
 * ## Usage:
 * - The `Environment` class is used to create environment instances, each identified by a unique `id`.
 * - It maintains a private mutable memory map, which can be interacted with using provided methods.
 *
 * ## Initialization:
 * Upon instantiation, the `Environment` object adds itself to a global `Domain.environments` collection,
 * which tracks all environments by their IDs.
 *
 * ## Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * fun main() {
 *     val env = Environment<String>("env1")
 *     env += 420 to "blaze it"
 * }
 * ```
 *
 * @param T the type of elements that can be stored in the memory of the environment.
 * @property id A unique string identifier for the environment.
 * @property memory An immutable view of the environment's memory. It provides access to the stored key-value pairs
 *   without allowing modifications to the underlying memory structure directly.
 * @constructor Creates a new environment with the given identifier and initializes its memory storage.
 * @see ExperimentalKeen for information on the experimental status of this class.
 */
@ExperimentalKeen
data class Environment<T>(val id: String) {
    init {
        Domain.environments += id to this
    }

    private val _memory = mutableMapOf<Int, T>()

    val memory: Map<Int, T> get() = _memory

    /**
     * Overloads the `+=` operator to add or update entries in the environment's memory. This function allows for a
     * convenient way to store or modify memory elements within the environment.
     *
     * ## Functionality:
     * - Takes a key-value pair as input, where the key is an `Int` and the value is of generic type `T`.
     * - Adds the key-value pair to the environment's memory. If the key already exists, its value is updated.
     *
     * ## Usage:
     * This operator is used to seamlessly add or update entries in the memory map of an `Environment` instance. It
     * enhances the ease of use by allowing direct assignment operations.
     *
     * ### Example:
     * ```
     * val env = Environment<String>("myEnvironment")
     * env += 1 to "Hello"
     * env += 2 to "World"
     * // The environment's memory now contains {1: "Hello", 2: "World"}
     * ```
     *
     * @param keyVal A Pair of an integer key and a value of type `T`, representing the memory element to be added or updated.
     */
    operator fun plusAssign(keyVal: Pair<Int, T>) {
        _memory += keyVal
    }
}

