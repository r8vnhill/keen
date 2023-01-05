package cl.ravenhill.keen.util.logging

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

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
 * @property outputChannel The output channel where the messages will be written to.
 * @param name The name of the logger.
 * @constructor Creates a new logger with the given name.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class Logger private constructor(private val name: String) {
    var outputChannel: OutputChannel = CompositeOutputChannel()
        private set
    var level: Level = Level.Info()

    /** Logs a message at the DEBUG level.  */
    fun debug(message: () -> String) =
        outputChannel.write(level.debug { "${msgMeta("DEBUG")} ${message()}" })

    /** Logs a message at the ERROR level.  */
    fun error(message: () -> String) =
        outputChannel.write(level.error { "${msgMeta("ERROR")} ${message()}" })

    /** Logs a message at the FATAL level.  */
    fun fatal(message: () -> String) =
        outputChannel.write(level.fatal { "${msgMeta("FATAL")} ${message()}" })

    /** Logs a message at the INFO level.  */
    fun info(message: () -> String) =
        outputChannel.write(level.info { "${msgMeta("INFO")} ${message()}" })

    /** Logs a message at the TRACE level.  */
    fun trace(message: () -> String) =
        outputChannel.write(level.trace { "${msgMeta("TRACE")} ${message()}" })

    /** Logs a message at the WARN level.  */
    fun warn(message: () -> String) =
        outputChannel.write(level.warn { "${msgMeta("WARN")} ${message()}" })

    private fun msgMeta(level: String): String = "${
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    } [${Thread.currentThread().name}] $level $name - "

    override fun toString() = "Logger { name: '$name' }"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Logger -> false
        Logger::class != other::class -> false
        name != other.name -> false
        else -> true
    }

    override fun hashCode() = Objects.hash(Logger::class, name)

    companion object {
        private val activeLoggers = mutableMapOf<String, Logger>()

        /** Returns a unique instance of a logger for the given name.   */
        fun instance(name: String) =
            activeLoggers.getOrDefault(name, Logger(name)).also { activeLoggers[name] = it }

        /** Returns true if a logger with the given name is active. */
        fun isActive(name: String): Boolean = name in activeLoggers

        /** Clears all active loggers.  */
        internal fun clearActiveLoggers() = activeLoggers.clear()
    }
}