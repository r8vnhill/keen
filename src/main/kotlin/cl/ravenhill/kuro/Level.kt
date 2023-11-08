/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.kuro

import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

/**
 * A sealed interface representing different logging levels.
 * Provides functions to log messages at different levels.
 * Nested classes override the log functions to return the message.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
@Deprecated("To be removed, prefer using evolution listeners and records")
sealed interface Level : Comparable<Level> {
    /** Returns an empty string.  */
    fun trace(message: () -> String) = ""

    /** Returns an empty string.  */
    fun debug(message: () -> String) = ""

    /** Returns an empty string.  */
    fun info(message: () -> String) = ""

    /** Returns an empty string.  */
    fun warn(message: () -> String) = ""

    /** Returns an empty string.  */
    fun error(message: () -> String) = ""

    /** Returns an empty string.  */
    fun fatal(message: () -> String): String

    /// Documentation inherited from [Comparable].
    override fun compareTo(other: Level) = when {
        this::class == other::class -> 0
        this::class.createType().isSupertypeOf(other::class.createType()) -> 1
        else -> -1
    }

    /** An _info_ level.  */
    open class Info : Warn() {
        /** Returns the info message.  */
        override fun info(message: () -> String) = message()

        override fun toString() = "INFO"
    }

    /** A _warn_ level.  */
    open class Warn : Error() {
        /** Returns the warn message.  */
        override fun warn(message: () -> String) = message()

        override fun toString() = "WARN"
    }

    /** An _error_ level.  */
    open class Error : Fatal() {
        /** Returns the error message.  */
        override fun error(message: () -> String) = message()

        override fun toString() = "ERROR"
    }

    /** A _fatal_ level.  */
    open class Fatal : AbstractLevel() {
        /** Returns the fatal message.  */
        override fun fatal(message: () -> String) = message()

        override fun toString() = "FATAL"
    }

    /**
     * An abstract class that implements the [Level] interface.
     * It overrides the [equals] and [hashCode] functions to compare the classes.
     */
    abstract class AbstractLevel : Level {
        /// Documentation inherited from [Any].
        override fun equals(other: Any?): Boolean = other is Level && this.compareTo(other) == 0

        /// Documentation inherited from [Any].
        override fun hashCode(): Int = this::class.hashCode()
    }
}