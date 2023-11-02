/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.Core.DEFAULT_MAX_PROGRAM_DEPTH
import cl.ravenhill.keen.Core.EvolutionLogger.DEFAULT_LEVEL
import cl.ravenhill.keen.Core.EvolutionLogger.level
import cl.ravenhill.keen.Core.EvolutionLogger.logger
import cl.ravenhill.keen.Core.maxProgramDepth
import cl.ravenhill.keen.Core.random
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.kuro.Level
import cl.ravenhill.kuro.logger
import cl.ravenhill.kuro.stdoutChannel
import kotlin.random.Random

/**
 * The `Core` object contains the functions and variables that are used by the rest of the library.
 *
 * @property logger The logger used by the library.
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
    var maxProgramDepth = DEFAULT_MAX_PROGRAM_DEPTH
        set(value) {
            enforce { "The maximum program depth must be positive" { value must BePositive } }
            field = value
        }
    var random: Random = Random.Default

    /**
     * Represents the "roll" of an n-dimensional or continuous dice.
     */
    object Dice {

        /**
         * Backing [Random] instance.
         */
        var random: Random = Random.Default
    }

    /**
     * A logger for tracking the evolution of [Evolver] instances.
     *
     * @property DEFAULT_LEVEL The default logging level ([Level.Warn]).
     * @property level The logging level. Defaults to [DEFAULT_LEVEL].
     * @property logger The logger instance used for logging.
     */
    object EvolutionLogger {
        val DEFAULT_LEVEL: Level = Level.Warn()
        var level: Level = DEFAULT_LEVEL
            set(value) {
                field = value
                logger.level = value
            }
        var logger = logger("Evolution") {
            level = Level.Warn()
            stdoutChannel()
        }

        /**
         * Logs a message at the Trace level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun trace(lazyMessage: () -> String) = logger.trace(lazyMessage)

        /**
         * Logs a message at the Debug level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun debug(lazyMessage: () -> String) = logger.debug(lazyMessage)

        /**
         * Logs a message at the Info level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun info(lazyMessage: () -> String) = logger.info(lazyMessage)

        /**
         * Logs a message at the Warn level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun warn(lazyMessage: () -> String) = logger.warn(lazyMessage)

        /**
         * Logs a message at the Error level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun error(lazyMessage: () -> String) = logger.error(lazyMessage)

        /**
         * Logs a message at the Fatal level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun fatal(lazyMessage: () -> String) = logger.fatal(lazyMessage)
    }
}

/**
 * Rolls a n-dimensional dice.
 */
fun Core.Dice.int(n: Int) = random.nextInt(n)

/**
 * Generates a new random double in [0, 1).
 */
fun Core.Dice.probability() = random.nextDouble()
