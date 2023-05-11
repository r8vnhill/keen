/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.problems.ga.stacktrace

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
    private val typeConstructors: MutableMap<KType, () -> Terminal<out Any>> = mutableMapOf(
        Int::class.createType() to { EphemeralConstant { Core.random.nextInt() } },
        Double::class.createType() to { EphemeralConstant { Core.random.nextDouble() } },
        Boolean::class.createType() to { EphemeralConstant { Core.random.nextBoolean() } },
        Char::class.createType() to { EphemeralConstant { Core.random.nextChar() } })

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
    fun random(): Sequence<Any?> = generateSequence {
        val returnType = types.random(Core.random)
        typeConstructors[returnType]?.invoke()
    }
}