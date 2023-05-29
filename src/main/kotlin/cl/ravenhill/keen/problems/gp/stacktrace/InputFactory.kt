/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.problems.gp.stacktrace

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.util.nextChar
import kotlin.reflect.KType
import kotlin.reflect.full.createType


/**
 * A factory class for generating inputs of different types.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
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
        Char::class.createType() to { Core.random.nextChar() })

    /**
     * Sets the constructor function for the specified input type.
     *
     * @param type The KType of the input type.
     * @param constructor The constructor function that generates a terminal of the input type.
     */
    operator fun set(type: KType, constructor: () -> Terminal<out Any>) {
        typeConstructors[type] = constructor
    }

    /**
     * Generates a random input of any registered type.
     *
     * @return A randomly generated input object.
     */
    fun random(type: KType): Sequence<Any?> = generateSequence {
        typeConstructors[type]?.invoke()
    }
}