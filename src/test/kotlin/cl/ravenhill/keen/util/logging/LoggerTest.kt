/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.shouldBeOfClass
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class LoggerTest : FreeSpec({
    "A logger" - {
        "should be able to be obtained by name" {
            checkAll(Arb.uniqueStrings()) { names ->
                names.forEach { Logger.instance(it) }
                Logger.activeLoggers.size shouldBe names.size
                names.forEach { Logger.activeLoggers[it] shouldBe Logger.instance(it) }
                Logger.clearActiveLoggers()
            }
        }

        "should have a default level of [Level.Info()]" {
            checkAll(Arb.loggers(Arb.list(Arb.string()))) { loggers ->
                loggers.forEach { it.level shouldBeOfClass Level.Info::class }
                Logger.clearActiveLoggers()
            }
        }

        "should be able to set the logging level" {
            checkAll(
                Arb.string(),
                Arb.element(
                    Level.Trace(),
                    Level.Debug(),
                    Level.Info(),
                    Level.Warn(),
                    Level.Error(),
                    Level.Fatal()
                )
            ) { name, level ->
                val logger = Logger.instance(name)
                logger.level = level
                logger.level shouldBe level
                Logger.clearActiveLoggers()
            }
        }

        "with [Level.Fatal] level should" - {
            "be able to log a fatal message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Fatal(),
                    Logger::fatal,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] FATAL .* - .*".toRegex()
                )
            }

            "not be able to log an error message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Fatal(),
                    Logger::error,
                    "^$".toRegex()
                )
            }

            "not be able to log a warning message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Fatal(),
                    Logger::warn,
                    "^$".toRegex()
                )
            }

            "not be able to log an info message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Fatal(),
                    Logger::info,
                    "^$".toRegex()
                )
            }

            "not be able to log a debug message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Fatal(),
                    Logger::debug,
                    "^$".toRegex()
                )
            }

            "not be able to log a trace message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Fatal(),
                    Logger::trace,
                    "^$".toRegex()
                )
            }
        }

        "with [Level.Error] level should" - {
            "be able to log a fatal message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Error(),
                    Logger::fatal,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] FATAL .* - .*".toRegex()
                )
            }

            "be able to log an error message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Error(),
                    Logger::error,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] ERROR .* - .*".toRegex()
                )
            }

            "not be able to log a warning message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Error(),
                    Logger::warn,
                    "^$".toRegex()
                )
            }

            "not be able to log an info message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Error(),
                    Logger::info,
                    "^$".toRegex()
                )
            }

            "not be able to log a debug message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Error(),
                    Logger::debug,
                    "^$".toRegex()
                )
            }

            "not be able to log a trace message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Error(),
                    Logger::trace,
                    "^$".toRegex()
                )
            }
        }

        "with [Level.Warn] level should" - {
            "be able to log a fatal message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Warn(),
                    Logger::fatal,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] FATAL .* - .*".toRegex()
                )
            }

            "be able to log an error message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Warn(),
                    Logger::error,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] ERROR .* - .*".toRegex()
                )
            }

            "be able to log a warning message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Warn(),
                    Logger::warn,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] WARN .* - .*".toRegex()
                )
            }

            "not be able to log an info message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Warn(),
                    Logger::info,
                    "^$".toRegex()
                )
            }

            "not be able to log a debug message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Warn(),
                    Logger::debug,
                    "^$".toRegex()
                )
            }

            "not be able to log a trace message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Warn(),
                    Logger::trace,
                    "^$".toRegex()
                )
            }
        }

        "with [Level.Info] level should" - {
            "be able to log a fatal message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Info(),
                    Logger::fatal,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] FATAL .* - .*".toRegex()
                )
            }

            "be able to log an error message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Info(),
                    Logger::error,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] ERROR .* - .*".toRegex()
                )
            }

            "be able to log a warning message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Info(),
                    Logger::warn,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] WARN .* - .*".toRegex()
                )
            }

            "be able to log an info message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Info(),
                    Logger::info,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] INFO .* - .*".toRegex()
                )
            }

            "not be able to log a debug message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Info(),
                    Logger::debug,
                    "^$".toRegex()
                )
            }

            "not be able to log a trace message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Info(),
                    Logger::trace,
                    "^$".toRegex()
                )
            }
        }

        "with [Level.Debug] level should" - {
            "be able to log a fatal message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Debug(),
                    Logger::fatal,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] FATAL .* - .*".toRegex()
                )
            }

            "be able to log an error message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Debug(),
                    Logger::error,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} \\[.*] ERROR .* - .*".toRegex()
                )
            }

            "be able to log a warning message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Debug(),
                    Logger::warn,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3} WARN .* - .*".toRegex()
                )
            }

            "be able to log an info message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Debug(),
                    Logger::info,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] INFO .* - .*".toRegex()
                )
            }

            "be able to log a debug message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Debug(),
                    Logger::debug,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] DEBUG .* - .*".toRegex()
                )
            }

            "not be able to log a trace message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Debug(),
                    Logger::trace,
                    "^$".toRegex()
                )
            }
        }

        "with [Level.Trace] level should" - {
            "be able to log a fatal message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Trace(),
                    Logger::fatal,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] FATAL .* - .*".toRegex()
                )
            }

            "be able to log an error message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Trace(),
                    Logger::error,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] ERROR .* - .*".toRegex()
                )
            }

            "be able to log a warning message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Trace(),
                    Logger::warn,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] WARN .* - .*".toRegex()
                )
            }

            "be able to log an info message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Trace(),
                    Logger::info,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] INFO .* - .*".toRegex()
                )
            }

            "be able to log a debug message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Trace(),
                    Logger::debug,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] DEBUG .* - .*".toRegex()
                )
            }

            "be able to log a trace message" {
                `check that the logger is able to log a message at the given level`(
                    Level.Trace(),
                    Logger::trace,
                    "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3} \\[.*] TRACE .* - .*".toRegex()
                )
            }
        }
    }

    "The list of active loggers should" - {
        "be empty when cleared" {
            checkAll(Arb.loggers(Arb.uniqueStrings())) { loggers ->
                Logger.activeLoggers.size shouldBe loggers.size
                Logger.clearActiveLoggers()
                Logger.activeLoggers.size shouldBe 0
            }
        }
    }
})

/**
 * Returns an arbitrary generator of a list of [Logger] instances based on a given [names] generator
 * of string lists.
 *
 * The returned generator creates a list of [Logger] instances with names generated by [names].
 * Note that creating an instance of a [Logger] automatically adds it to the [Logger.activeLoggers]
 * list.
 * This function can be used to generate a list of loggers for testing purposes.
 *
 * @param names The generator used to create the list of logger names.
 * @return An arbitrary generator of a list of [Logger] instances.
 */
private fun Arb.Companion.loggers(names: Arb<List<String>>) = arbitrary {
    names.bind().map { Logger.instance(it) }
}

/**
 * Returns an arbitrary generator of a list of unique strings.
 * The function generates a set of strings and then converts it to a list, ensuring that
 * there are no repeated strings in the output.
 *
 * @return An arbitrary generator of a list of unique strings.
 */
private fun Arb.Companion.uniqueStrings() = arbitrary {
    set(string()).bind().toList()
}

private suspend fun `check that the logger is able to log a message at the given level`(
    level: Level,
    method: Logger.(message: () -> String) -> Unit,
    regex: Regex
) {
    checkAll(
        Arb.string(),
        Arb.string(),
        Arb.bufferedOutputChannel()
    ) { name, message, channel ->
        val logger = Logger.instance(name)
        logger.outputChannel.add(channel)
        logger.level = level
        logger.method { message }
        channel.toString() shouldMatch regex
        channel.clear()
        Logger.clearActiveLoggers()
    }
}