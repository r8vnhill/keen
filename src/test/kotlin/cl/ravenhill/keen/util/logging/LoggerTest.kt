/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.logPattern
import cl.ravenhill.keen.logger
import cl.ravenhill.keen.loggers
import cl.ravenhill.keen.uniqueStrings
import cl.ravenhill.keen.util.Clearable
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


/**
 * Clears all objects in the given collection that implement the [Clearable] interface.
 */
private fun <T : Clearable<T>> clearAll(clearables: Iterable<Clearable<T>>) =
    clearables.forEach { it.clear() }

/**
 * Applies the given [function] to each element of the iterable.
 *
 * This function applies the given [function] to each element of the iterable, using the `apply`
 * method to apply the function in the context of the element.
 * This allows for a more concise and readable syntax when applying the same operation to each
 * element of the iterable.
 *
 * @param function The function to apply to each element of the iterable.
 */
private fun <T> Iterable<T>.applyForEach(function: T.() -> Unit) = forEach {
    it.apply { function() }
}

/**
 * Performs checks to verify that the logger is able to log a message at the given level.
 *
 * @param level the level at which the logger should log the message.
 * @param method the logging method to use on the logger.
 * @param regex the regular expression to match against the logged message.
 */
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
        logger.compositeChannel.add(channel)
        logger.level = level
        logger.method { message }
        channel.toString() shouldMatch regex
        channel.clear()
        Logger.clearActiveLoggers()
    }
}

class LoggerTest : FreeSpec({
    "A [Logger]" - {
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
                assertSoftly {
                    loggers.forEach { it.level.shouldBeInstanceOf<Level.Info>() }
                }
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

        "with level" - {
            "[Level.Fatal] should" - {
                "be able to log a fatal message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Fatal(),
                        Logger::fatal,
                        logPattern("FATAL")
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

            "[Level.Error] should" - {
                "be able to log a fatal message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Error(),
                        Logger::fatal,
                        logPattern("FATAL")
                    )
                }

                "be able to log an error message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Error(),
                        Logger::error,
                        logPattern("ERROR")
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

            "[Level.Warn] should" - {
                "be able to log a fatal message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Warn(),
                        Logger::fatal,
                        logPattern("FATAL")
                    )
                }

                "be able to log an error message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Warn(),
                        Logger::error,
                        logPattern("ERROR")
                    )
                }

                "be able to log a warning message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Warn(),
                        Logger::warn,
                        logPattern("WARN")
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

            "[Level.Info] should" - {
                "be able to log a fatal message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Info(),
                        Logger::fatal,
                        logPattern("FATAL")
                    )
                }

                "be able to log an error message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Info(),
                        Logger::error,
                        logPattern("ERROR")
                    )
                }

                "be able to log a warning message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Info(),
                        Logger::warn,
                        logPattern("WARN")
                    )
                }

                "be able to log an info message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Info(),
                        Logger::info,
                        logPattern("INFO")
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

            "[Level.Debug] should" - {
                "be able to log a fatal message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Debug(),
                        Logger::fatal,
                        logPattern("FATAL")
                    )
                }

                "be able to log an error message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Debug(),
                        Logger::error,
                        logPattern("ERROR")
                    )
                }

                "be able to log a warning message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Debug(),
                        Logger::warn,
                        logPattern("WARN")
                    )
                }

                "be able to log an info message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Debug(),
                        Logger::info,
                        logPattern("INFO")
                    )
                }

                "be able to log a debug message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Debug(),
                        Logger::debug,
                        logPattern("DEBUG")
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

            "[Level.Trace] should" - {
                "be able to log a fatal message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Trace(),
                        Logger::fatal,
                        logPattern("FATAL")
                    )
                }

                "be able to log an error message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Trace(),
                        Logger::error,
                        logPattern("ERROR")
                    )
                }

                "be able to log a warning message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Trace(),
                        Logger::warn,
                        logPattern("WARN")
                    )
                }

                "be able to log an info message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Trace(),
                        Logger::info,
                        logPattern("INFO")
                    )
                }

                "be able to log a debug message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Trace(),
                        Logger::debug,
                        logPattern("DEBUG")
                    )
                }

                "be able to log a trace message" {
                    `check that the logger is able to log a message at the given level`(
                        Level.Trace(),
                        Logger::trace,
                        logPattern("TRACE")
                    )
                }
            }
        }

        "when converting to [String] should return a string representation of the [Logger]" {
            checkAll(Arb.logger(Arb.string())) { logger ->
                "$logger" shouldBe "Logger(name='${logger.name}', level=${logger.level}, outputChannel=${logger.compositeChannel})"
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

    "A [BufferedOutputChannel] can be added to the [Logger]" {
        checkAll(Arb.loggers(Arb.uniqueStrings())) { loggers ->
            assertSoftly {
                loggers.applyForEach {
                    bufferedOutputChannel()
                    compositeChannel.outputChannels.first()
                        .shouldBeInstanceOf<BufferedOutputChannel>()
                }
            }
            clearAll(loggers)
        }
    }

    "A [FileOutputChannel] can be added to the [Logger]" {
        checkAll(Arb.loggers(Arb.uniqueStrings()), Arb.filename()) { loggers, filename ->
            assertSoftly {
                loggers.applyForEach {
                    fileChannel { this.filename = filename }
                    compositeChannel.outputChannels.first()
                        .shouldBeInstanceOf<FileOutputChannel>()
                }
            }
            clearAll(loggers)
        }
    }

    "A [StdoutChannel] can be added to the [Logger]" {
        checkAll(Arb.loggers(Arb.uniqueStrings())) { loggers ->
            assertSoftly {
                loggers.applyForEach {
                    stdoutChannel()
                    compositeChannel.outputChannels.first()
                        .shouldBeInstanceOf<StdoutChannel>()
                }
            }
            clearAll(loggers)
        }
    }

    "A [Logger] can be created with the builder function" {
        checkAll(Arb.uniqueStrings()) { names ->
            assertSoftly {
                names.forEach {
                    val logger = logger(it) {
                        bufferedOutputChannel()
                    }
                    logger.name shouldBe it
                    logger.compositeChannel.outputChannels.first()
                        .shouldBeInstanceOf<BufferedOutputChannel>()
                }
            }
        }
    }
})
