/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.kuro

import cl.ravenhill.keen.util.Clearable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Creates a new logger.
 *
 * @param name The name of the logger.
 * @param builder Builder block for the logger.
 *
 * @return A new logger.
 */
fun logger(name: String, builder: Logger.() -> Unit) =
    Logger.instance(name).also { it.builder() }

/**
 * Entity that logs messages to an output channel.
 *
 * @property compositeChannel The output channel where the messages will be written to.
 * @property level The logging level. Messages with a level lower than this will not be logged.
 *  Defaults to [Level.Info].
 * @param name The name of the logger.
 * @constructor Creates a new logger with the given name.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class Logger private constructor(val name: String) : Clearable<Logger> {
    val compositeChannel = CompositeOutputChannel()
    var level: Level = Level.Info()

    /** Logs a message at the DEBUG level.  */
    fun debug(message: () -> String) =
        compositeChannel.write(level.debug { "${msgMeta("DEBUG")} ${message()}" })

    /** Logs a message at the ERROR level.  */
    fun error(message: () -> String) =
        compositeChannel.write(level.error { "${msgMeta("ERROR")} ${message()}" })

    /** Logs a message at the FATAL level.  */
    fun fatal(message: () -> String) =
        compositeChannel.write(level.fatal { "${msgMeta("FATAL")} ${message()}" })

    /** Logs a message at the INFO level.  */
    fun info(message: () -> String) =
        compositeChannel.write(level.info { "${msgMeta("INFO")} ${message()}" })

    /** Logs a message at the TRACE level.  */
    fun trace(message: () -> String) =
        compositeChannel.write(level.trace { "${msgMeta("TRACE")} ${message()}" })

    /** Logs a message at the WARN level.  */
    fun warn(message: () -> String) =
        compositeChannel.write(level.warn { "${msgMeta("WARN")} ${message()}" })

    /**
     * Formats a log message metadata string with the given logging level.
     * The metadata string includes the current timestamp, the name of the current thread,
     * and the logging level name.
     *
     * @param level the logging level
     * @return the formatted metadata string
     */
    private fun msgMeta(level: String): String = "${
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    } [${Thread.currentThread().name}] $level $name - "

    /// Documentation inherited from [Clearable].
    override fun clear(): Logger {
        compositeChannel.clear()
        _activeLoggers.remove(name)
        return this
    }

    override fun toString() = "Logger(name='$name', level=$level, outputChannel=$compositeChannel)"

    companion object {
        data object
        /**
         * A map of all active loggers with their names as keys and instances of [Logger] as values.
         * `internal` for testing, should not be accessed directly
         */
        internal val _activeLoggers = mutableMapOf<String, Logger>()

        /**
         * An immutable map of all active loggers with their names as keys and instances of [Logger]
         * as values.
         */
        val activeLoggers: Map<String, Logger> get() = _activeLoggers

        /**
         * Returns a unique instance of a logger for the given name.
         *
         * @param name The name of the logger.
         * @return An instance of [Logger] for the given name.
         */
        fun instance(name: String) =
            _activeLoggers.getOrDefault(name, Logger(name)).also { _activeLoggers[name] = it }

        /**
         * Clears all active loggers.
         */
        internal fun clearActiveLoggers() = _activeLoggers.clear()
    }
}
