package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.annotations.ExperimentalKeen

/**
 * Represents a programmable environment with a unique identifier, capable of storing and managing memory elements.
 * This class is a component of the Keen library and is marked as experimental.
 *
 * ## Usage:
 * - Used for creating instances of programmable environments, where each environment is identified by a unique [id].
 * - Maintains a private mutable map to simulate memory, allowing storage and retrieval of key-value pairs.
 * - Integral for testing or simulating scenarios where dynamic memory allocation and access are required.
 *
 * ## Initialization:
 * - Upon instantiation, the `Environment` object registers itself in the global [Domain.environments] collection.
 * - This collection maintains a record of all environments, facilitating tracking and management.
 *
 * ## Example:
 * ```kotlin
 * @OptIn(ExperimentalKeen::class)
 * fun main() {
 *     val env = Environment<String>("env1")
 *     env += 420 to "blaze it" // Add a new memory element
 *     env -= 420              // Remove a memory element
 *     val value = env[420]    // Retrieve a memory element
 * }
 * ```
 *
 * @param T The type of elements that can be stored in the environment's memory.
 * @property id A unique string identifier for the environment.
 * @constructor Creates a new environment with the specified identifier and initializes its memory storage.
 * @see ExperimentalKeen for information on the experimental status of this class.
 */
@ExperimentalKeen
data class Environment<T>(val id: String) : MutableMap<Int, T> {

    init {
        Domain.environments += id to this
    }

    /**
     * A private mutable map representing the memory of the environment.
     *
     * This map stores key-value pairs where the key is an `Int` and the value is of type `T`. It is used internally
     * within the `Environment` class to manage the state and data associated with the environment.
     */
    private val _memory = mutableMapOf<Int, T>()

    /**
     * Provides mutable access to the memory entries of the environment.
     *
     * This property overrides the `entries` member from the MutableMap interface, allowing direct access to the
     * mutable set of key-value pairs stored in the environment's memory. It enables iteration and modification
     * of these entries, facilitating flexible manipulation of the environment's state.
     *
     * @return A mutable set of map entries, each entry representing a key-value pair in the environment's memory.
     */
    override val entries: MutableSet<MutableMap.MutableEntry<Int, T>> get() = _memory.entries

    /**
     * Provides mutable access to the set of keys in the environment's memory.
     *
     * This property, part of the `Environment` class, overrides the `keys` member from the MutableMap interface.
     * It grants direct access to the mutable set of keys in the environment's memory, allowing for iteration,
     * addition, and removal of keys. This feature is crucial for dynamically managing the keys within the
     * environment's memory, especially in scenarios requiring modification of the memory's structure.
     *
     * @return A mutable set of integers representing the keys in the environment's memory.
     */
    override val keys: MutableSet<Int> get() = _memory.keys

    /**
     * Returns the number of key-value pairs stored in the environment's memory.
     *
     * This property overrides the `size` member from the Map interface and provides a quick way to determine
     * the number of entries currently stored in the environment's memory. It reflects the current state of
     * the memory, aiding in understanding its load and capacity at any given point.
     *
     * @return The total count of key-value pairs in the environment's memory as an `Int`.
     */
    override val size: Int get() = _memory.size

    /**
     * Provides mutable access to the collection of values in the environment's memory.
     *
     * This property overrides the `values` member from the MutableMap interface. It allows access to the mutable
     * collection of values (of type `T`) stored in the environment's memory. This is useful for iterating over,
     * adding, or removing values directly, facilitating dynamic manipulation and inspection of the environment's
     * state from a values-centric perspective.
     *
     * @return A mutable collection of type `T` representing the values stored in the environment's memory.
     */
    override val values: MutableCollection<T> get() = _memory.values

    /**
     * Retrieves a value from the environment's memory based on the provided key.
     *
     * This function overrides the `get` operator from the Map interface. It is used to access the value associated
     * with a specific key in the environment's memory. If the key is present in the memory, the corresponding value
     * is returned; otherwise, `null` is returned.
     *
     * ## Usage:
     * This operator provides a concise and readable way to access values in the memory, making it straightforward
     * to retrieve data based on a key.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnv")
     * env += 1 to "Data"
     * val value = env[1]  // Retrieves "Data"
     * val nonExistent = env[2]  // Returns null as key 2 is not present
     * ```
     *
     * @param key The integer key for which the value is to be retrieved.
     * @return The value associated with the given key if it exists, or `null` otherwise.
     */
    override operator fun get(key: Int): T? = _memory[key]

    /**
     * Clears all key-value pairs from the environment's memory.
     *
     * This method overrides the `clear` function from the MutableMap interface. It is used to remove all entries
     * from the environment's memory, effectively resetting it to an empty state. This operation is useful when
     * the entire memory needs to be refreshed or reinitialized without creating a new environment instance.
     *
     * ## Usage:
     * Invoke this method to clear the environment's memory, removing all stored data.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnv")
     * env += 1 to "Data"
     * // Memory contains data
     * env.clear()
     * // Memory is now empty
     * ```
     */
    override fun clear() = _memory.clear()

    /**
     * Checks if the environment's memory is empty.
     *
     * This method overrides the `isEmpty` function from the Map interface. It provides a simple way to check
     * whether the environment's memory has no key-value pairs stored in it. It returns `true` if the memory is empty,
     * and `false` otherwise.
     *
     * ## Usage:
     * Use this method to quickly determine if the environment's memory contains any data.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnv")
     * val initiallyEmpty = env.isEmpty() // Returns true if no data is present
     * env += 1 to "Data"
     * val notEmptyAnymore = env.isEmpty() // Returns false as memory now contains data
     * ```
     *
     * @return `true` if the memory is empty, `false` otherwise.
     */
    override fun isEmpty() = _memory.isEmpty()

    /**
     * Removes the entry with the specified key from the environment's memory.
     *
     * This method overrides the `remove` function from the MutableMap interface. It is used to delete a specific
     * key-value pair from the environment's memory based on the provided key. If the key is found in the memory,
     * the corresponding entry is removed and its value is returned; if the key is not present, the function returns
     * `null`.
     *
     * ## Usage:
     * This method is useful for selectively removing entries from the memory when their keys are known, without
     * affecting the rest of the memory state.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnv")
     * env += 1 to "Data"
     * val removedValue = env.remove(1) // Removes the entry with key 1 and returns "Data"
     * val nonExistent = env.remove(2) // Returns null as key 2 is not present
     * ```
     *
     * @param key The integer key of the entry to be removed from the memory.
     * @return The value associated with the removed key, or `null` if the key was not present in the memory.
     */
    override fun remove(key: Int) = _memory.remove(key)

    /**
     * Adds all entries from the specified map to the environment's memory.
     *
     * This method overrides the `putAll` function from the MutableMap interface. It is used to bulk insert or
     * update entries in the environment's memory using a provided map. Each entry in the `from` map is either
     * added to the memory or updates an existing entry if the key already exists.
     *
     * ## Usage:
     * This method is useful for initializing or updating the environment's memory with multiple entries at once,
     * thereby enhancing efficiency and convenience compared to individual insertions.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnvironment")
     * val newEntries = mapOf(1 to "Data1", 2 to "Data2")
     * env.putAll(newEntries)
     * // The environment's memory now contains entries from newEntries
     * ```
     *
     * @param from The map containing entries to be added or updated in the environment's memory. Keys are of type
     *   `Int`, and values are of type `T`.
     */
    override fun putAll(from: Map<out Int, T>) = _memory.putAll(from)

    /**
     * Inserts or updates an entry in the environment's memory.
     *
     * This method overrides the `put` function from the MutableMap interface. It is used to add a new entry or
     * update an existing entry in the environment's memory with the specified key and value. If the key already
     * exists in the memory, its associated value is replaced with the new value. If the key does not exist, a
     * new entry is created.
     *
     * ## Usage:
     * Utilize this method to insert or modify individual entries in the environment's memory. It offers a direct
     * and straightforward way to manage the memory content.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnvironment")
     * env.put(1, "Data1") // Adds a new entry with key 1 and value "Data1"
     * env.put(1, "Data2") // Updates the value of the entry with key 1 to "Data2"
     * ```
     *
     * @param key The integer key of the entry to be added or updated.
     * @param value The value of type `T` to be associated with the key.
     * @return The previous value associated with the key, or `null` if there was no mapping for the key.
     */
    override fun put(key: Int, value: T) = _memory.put(key, value)

    /**
     * Checks if the environment's memory contains the specified value.
     *
     * This method overrides the `containsValue` function from the Map interface. It is used to determine
     * whether the environment's memory contains at least one entry with the specified value. The method
     * returns `true` if the value is found, and `false` otherwise.
     *
     * ## Usage:
     * Utilize this method to verify if a specific value exists within the environment's memory, irrespective
     * of its associated key. This is particularly useful for scenarios where the presence of a value is
     * more important than its key.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnvironment")
     * env.put(1, "Data1")
     * val containsValue = env.containsValue("Data1") // Returns true
     * val doesNotContainValue = env.containsValue("Data2") // Returns false
     * ```
     *
     * @param value The value of type `T` to check for in the environment's memory.
     * @return `true` if the memory contains the specified value, `false` otherwise.
     */
    override fun containsValue(value: T) = _memory.containsValue(value)

    /**
     * Checks if the environment's memory contains the specified key.
     *
     * This method overrides the `containsKey` function from the Map interface. It is used to determine
     * whether the environment's memory has an entry with the specified key. If the key is present,
     * the method returns `true`; otherwise, it returns `false`.
     *
     * ## Usage:
     * Use this method to verify the existence of a specific key in the environment's memory. This can be
     * particularly helpful in scenarios where you need to check the presence of a key before performing
     * operations dependent on its existence.
     *
     * ### Example:
     * ```kotlin
     * val env = Environment<String>("exampleEnvironment")
     * env.put(1, "Data1")
     * val containsKey = env.containsKey(1) // Returns true since key 1 is present
     * val doesNotContainKey = env.containsKey(2) // Returns false as key 2 is not present
     * ```
     *
     * @param key The integer key to check for in the environment's memory.
     * @return `true` if the memory contains an entry for the specified key, `false` otherwise.
     */
    override fun containsKey(key: Int) = _memory.containsKey(key)
}
