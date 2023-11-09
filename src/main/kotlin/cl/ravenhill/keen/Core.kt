/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.Core.DEFAULT_MAX_PROGRAM_DEPTH
import cl.ravenhill.keen.Core.maxProgramDepth
import cl.ravenhill.keen.Core.random
import cl.ravenhill.keen.prog.Environment
import kotlin.random.Random

/**
 * The `Core` object contains the functions and variables that are used by the rest of the library.
 *
 * @property maxProgramDepth The maximum depth of a program tree.
 * @property DEFAULT_MAX_PROGRAM_DEPTH The default maximum depth of a program tree (7).
 * @property random The random number generator.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
object Core {

    const val DEFAULT_MAX_PROGRAM_DEPTH = 7

    val environments: MutableMap<String, Environment<*>> = mutableMapOf()

    var maxProgramDepth = DEFAULT_MAX_PROGRAM_DEPTH
        set(value) {
            constraints { "The maximum program depth [$value] must be positive" { value must BePositive } }
            field = value
        }
    var random: Random = Random.Default

    /**
     * Represents the "roll" of an n-dimensional or continuous dice.
     */
    @Deprecated("To be removed, prefer using Core.random")
    object Dice {

        /**
         * Backing [Random] instance.
         */
        var random: Random = Random.Default
    }
}

/**
 * Rolls a n-dimensional dice.
 */
fun Core.Dice.int(n: Int) = random.nextInt(n)
