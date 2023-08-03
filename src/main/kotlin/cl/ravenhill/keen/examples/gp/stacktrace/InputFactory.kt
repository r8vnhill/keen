/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.examples.gp.stacktrace

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.nextChar
import cl.ravenhill.keen.util.nextString
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.withNullability


/**
 * A factory class for generating inputs of different types.
 *
 * This class provides functionality to generate inputs of various types. It includes a map that associates
 * each input type with a constructor function that generates a terminal of that type. Additionally, it
 * allows registering custom constructor functions for specific input types.
 *
 * @property types The list of KTypes of input types.
 * @property typeConstructors A map that associates each input type with a constructor function that generates
 *                           a terminal of that type.
 * @throws IllegalArgumentException If no constructor is found for the specified input type.
 **************************************************************************************************/
class InputFactory {
    /**
     * The list of KTypes of input types.
     */
    private val types: Set<KType> get() = typeConstructors.keys

    /**
     * A map that associates each input type with a constructor function that generates a terminal
     * of that type.
     */
    private val typeConstructors: MutableMap<KType, () -> Any> = mutableMapOf(
        Int::class.createType() to { Core.random.nextInt() },
        Double::class.createType() to { Core.random.nextDouble() },
        Boolean::class.createType() to { Core.random.nextBoolean() },
        Char::class.createType() to { Core.random.nextChar() },
        String::class.createType() to { Core.random.nextString() }
    )

    /**
     * Gets the constructor function for the specified input type.
     *
     * @param type The KType of the input type.
     * @return The constructor function for the specified input type.
     * @throws IllegalArgumentException If no constructor is found for the specified input type.
     */
    operator fun get(type: KType): () -> Any? {
        if (type.isMarkedNullable) {
            if (Core.random.nextBoolean()) {
                return { null }
            }
        }
        return typeConstructors[type.withNullability(false)]
            ?: throw IllegalArgumentException("No constructor for type $type.")
    }

    /**
     * Sets the constructor function for the specified input type.
     *
     * @param type The KType of the input type.
     * @param constructor The constructor function that generates a terminal of the input type.
     */
    operator fun set(type: KType, constructor: () -> Any) {
        typeConstructors[type] = constructor
    }

    /**
     * Generates a sequence of randomly generated input objects for the specified type.
     *
     * @param type The KType of the input.
     * @return A sequence of randomly generated input objects.
     */
    fun random(type: KType): Sequence<Any?> = generateSequence {
        typeConstructors[type]?.invoke()
    }
}
