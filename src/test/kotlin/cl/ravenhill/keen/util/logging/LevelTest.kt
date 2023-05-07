/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.checkAll


class LevelTest : FreeSpec({
    lateinit var fatal: Level.Fatal
    lateinit var error: Level.Error
    lateinit var warn: Level.Warn
    lateinit var info: Level.Info
    lateinit var debug: Level.Debug
    lateinit var trace: Level.Trace

    beforeEach {
        fatal = Level.Fatal()
        error = Level.Error()
        warn = Level.Warn()
        info = Level.Info()
        debug = Level.Debug()
        trace = Level.Trace()
    }

    "A fatal level" - {
        "comparison should" - {
            "be greater than any other level" {
                fatal shouldBeGreaterThanAll listOf(
                    error,
                    warn,
                    info,
                    debug,
                    trace
                )
            }

            "be equal to another fatal level" {
                fatal shouldBe Level.Fatal()
            }

            "not be equal to any other level" {
                fatal shouldNotBeAnyOf listOf(error, warn, info, debug, trace)
            }
        }

        "hashing should" - {
            "be equal to another fatal level" {
                fatal.hashCode() shouldBe Level.Fatal().hashCode()
            }

            "not be equal to any other level" {
                fatal.hashCode() shouldNotBeAnyOf listOf(
                    error.hashCode(),
                    warn.hashCode(),
                    info.hashCode(),
                    debug.hashCode(),
                    trace.hashCode()
                )
            }
        }

        "should return the message when logging a fatal message" {
            checkAll<String> {
                fatal.fatal { it } shouldBe it
            }
        }

        "should return an empty string when logging" - {
            "an error message" {
                checkAll<String> {
                    fatal.error { it } shouldBe ""
                }
            }

            "a warn message" {
                checkAll<String> {
                    fatal.warn { it } shouldBe ""
                }
            }

            "an info message" {
                checkAll<String> {
                    fatal.info { it } shouldBe ""
                }
            }

            "a debug message" {
                checkAll<String> {
                    fatal.debug { it } shouldBe ""
                }
            }

            "a trace message" {
                checkAll<String> {
                    fatal.trace { it } shouldBe ""
                }
            }
        }
    }

    "An error level" - {
        "comparison should" - {
            "be greater than any other level except fatal" {
                error shouldBeGreaterThanAll listOf(
                    warn,
                    info,
                    debug,
                    trace
                )
            }

            "be less than fatal" {
                error shouldBeLessThan fatal
            }

            "be equal to another error level" {
                error shouldBe Level.Error()
            }

            "not be equal to any other level" {
                error shouldNotBeAnyOf listOf(fatal, warn, info, debug, trace)
            }
        }

        "hashing should" - {
            "be equal to another error level" {
                error.hashCode() shouldBe Level.Error().hashCode()
            }

            "not be equal to any other level" {
                error.hashCode() shouldNotBeAnyOf listOf(
                    fatal.hashCode(),
                    warn.hashCode(),
                    info.hashCode(),
                    debug.hashCode(),
                    trace.hashCode()
                )
            }
        }

        "should return the message when logging" - {
            "a fatal message" {
                checkAll<String> {
                    error.fatal { it } shouldBe it
                }
            }

            "an error message" {
                checkAll<String> {
                    error.error { it } shouldBe it
                }
            }
        }

        "should return an empty string when logging" - {
            "a warn message" {
                checkAll<String> {
                    error.warn { it } shouldBe ""
                }
            }

            "an info message" {
                checkAll<String> {
                    error.info { it } shouldBe ""
                }
            }

            "a debug message" {
                checkAll<String> {
                    error.debug { it } shouldBe ""
                }
            }

            "a trace message" {
                checkAll<String> {
                    error.trace { it } shouldBe ""
                }
            }
        }
    }

    "A warn level" - {
        "comparison should" - {
            "be greater than any other level except fatal and error" {
                warn shouldBeGreaterThanAll listOf(
                    info,
                    debug,
                    trace
                )
            }

            "be less than fatal and error" {
                warn shouldBeLessThanAll listOf(
                    fatal,
                    error
                )
            }

            "be equal to another warn level" {
                warn shouldBe Level.Warn()
            }

            "not be equal to any other level" {
                warn shouldNotBeAnyOf listOf(fatal, error, info, debug, trace)
            }
        }

        "hashing should" - {
            "be equal to another warn level" {
                warn.hashCode() shouldBe Level.Warn().hashCode()
            }

            "not be equal to any other level" {
                warn.hashCode() shouldNotBeAnyOf listOf(
                    fatal.hashCode(),
                    error.hashCode(),
                    info.hashCode(),
                    debug.hashCode(),
                    trace.hashCode()
                )
            }
        }

        "should return the message when logging" - {
            "a fatal message" {
                checkAll<String> {
                    warn.fatal { it } shouldBe it
                }
            }

            "an error message" {
                checkAll<String> {
                    warn.error { it } shouldBe it
                }
            }

            "a warn message" {
                checkAll<String> {
                    warn.warn { it } shouldBe it
                }
            }
        }

        "should return an empty string when logging" - {
            "an info message" {
                checkAll<String> {
                    warn.info { it } shouldBe ""
                }
            }

            "a debug message" {
                checkAll<String> {
                    warn.debug { it } shouldBe ""
                }
            }

            "a trace message" {
                checkAll<String> {
                    warn.trace { it } shouldBe ""
                }
            }
        }
    }

    "An info level" - {
        "comparison should" - {
            "be greater than any other level except fatal, error, and warn" {
                info shouldBeGreaterThanAll listOf(
                    debug,
                    trace
                )
            }

            "be less than fatal, error, and warn" {
                info shouldBeLessThanAll listOf(
                    fatal,
                    error,
                    warn
                )
            }

            "be equal to another info level" {
                info shouldBe Level.Info()
            }

            "not be equal to any other level" {
                info shouldNotBeAnyOf listOf(fatal, error, warn, debug, trace)
            }
        }

        "hashing should" - {
            "be equal to another info level" {
                info.hashCode() shouldBe Level.Info().hashCode()
            }

            "not be equal to any other level" {
                info.hashCode() shouldNotBeAnyOf listOf(
                    fatal.hashCode(),
                    error.hashCode(),
                    warn.hashCode(),
                    debug.hashCode(),
                    trace.hashCode()
                )
            }
        }

        "should return the message when logging" - {
            "a fatal message" {
                checkAll<String> {
                    info.fatal { it } shouldBe it
                }
            }

            "an error message" {
                checkAll<String> {
                    info.error { it } shouldBe it
                }
            }

            "a warn message" {
                checkAll<String> {
                    info.warn { it } shouldBe it
                }
            }

            "an info message" {
                checkAll<String> {
                    info.info { it } shouldBe it
                }
            }
        }

        "should return an empty string when logging" - {
            "a debug message" {
                checkAll<String> {
                    info.debug { it } shouldBe ""
                }
            }

            "a trace message" {
                checkAll<String> {
                    info.trace { it } shouldBe ""
                }
            }
        }
    }

    "A debug level" - {
        "comparison should" - {
            "be greater than any other level except fatal, error, warn, and info" {
                debug shouldBeGreaterThanAll listOf(
                    trace
                )
            }

            "be less than fatal, error, warn, and info" {
                debug shouldBeLessThanAll listOf(
                    fatal,
                    error,
                    warn,
                    info
                )
            }

            "be equal to another debug level" {
                debug shouldBe Level.Debug()
            }

            "not be equal to any other level" {
                debug shouldNotBeAnyOf listOf(fatal, error, warn, info, trace)
            }
        }

        "hashing should" - {
            "be equal to another debug level" {
                debug.hashCode() shouldBe Level.Debug().hashCode()
            }

            "not be equal to any other level" {
                debug.hashCode() shouldNotBeAnyOf listOf(
                    fatal.hashCode(),
                    error.hashCode(),
                    warn.hashCode(),
                    info.hashCode(),
                    trace.hashCode()
                )
            }
        }

        "should return the message when logging" - {
            "a fatal message" {
                checkAll<String> {
                    debug.fatal { it } shouldBe it
                }
            }

            "an error message" {
                checkAll<String> {
                    debug.error { it } shouldBe it
                }
            }

            "a warn message" {
                checkAll<String> {
                    debug.warn { it } shouldBe it
                }
            }

            "an info message" {
                checkAll<String> {
                    debug.info { it } shouldBe it
                }
            }

            "a debug message" {
                checkAll<String> {
                    debug.debug { it } shouldBe it
                }
            }
        }

        "should return an empty string when logging" - {
            "a trace message" {
                checkAll<String> {
                    debug.trace { it } shouldBe ""
                }
            }
        }
    }

    "A trace level" - {
        "comparison should" - {
            "be less than any other level" {
                trace shouldBeLessThanAll listOf(
                    fatal,
                    error,
                    warn,
                    info,
                    debug
                )
            }

            "be equal to another trace level" {
                trace shouldBe Level.Trace()
            }

            "not be equal to any other level" {
                trace shouldNotBeAnyOf listOf(fatal, error, warn, info, debug)
            }
        }

        "hashing should" - {
            "be equal to another [Trace] level" {
                trace shouldHaveSameHashCodeAs Level.Trace()
            }

            "not be equal to any other level" {
                trace.hashCode() shouldNotBeAnyOf listOf(
                    fatal.hashCode(),
                    error.hashCode(),
                    warn.hashCode(),
                    info.hashCode(),
                    debug.hashCode()
                )
            }
        }

        "should return the message when logging" - {
            "a fatal message" {
                checkAll<String> {
                    trace.fatal { it } shouldBe it
                }
            }

            "an error message" {
                checkAll<String> {
                    trace.error { it } shouldBe it
                }
            }

            "a warn message" {
                checkAll<String> {
                    trace.warn { it } shouldBe it
                }
            }

            "an info message" {
                checkAll<String> {
                    trace.info { it } shouldBe it
                }
            }

            "a debug message" {
                checkAll<String> {
                    trace.debug { it } shouldBe it
                }
            }

            "a trace message" {
                checkAll<String> {
                    trace.trace { it } shouldBe it
                }
            }
        }
    }
})

/**
 * Asserts that a [Level] instance should not be equal to any of the levels in the given
 * list.
 *
 * @param levels the list of levels to compare against.
 */
private infix fun <T> T.shouldNotBeAnyOf(levels: List<T>) = assertSoftly {
    levels.forEach {
        this shouldNotBe it
    }
}

/**
 * Asserts that a value is greater than all other values in a list of comparable values.
 *
 * @param others a list of comparable values to compare against the receiver.
 * @throws AssertionError if the receiver is not greater than all other values.
 */
private infix fun <T : Comparable<T>> T.shouldBeGreaterThanAll(others: List<T>) = assertSoftly {
    others.forEach {
        this shouldBeGreaterThan it
    }
}

/**
 * Asserts that a value is less than all other values in a list of comparable values.
 *
 * @param others a list of comparable values to compare against the receiver.
 * @throws AssertionError if the receiver is not less than all other values.
 */
private infix fun <T : Comparable<T>> T.shouldBeLessThanAll(others: List<T>) = assertSoftly {
    others.forEach {
        this shouldBeLessThan it
    }
}