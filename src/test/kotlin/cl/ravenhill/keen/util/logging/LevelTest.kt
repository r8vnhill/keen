/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
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