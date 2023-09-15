/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.kuro.Level
import cl.ravenhill.kuro.Logger
import cl.ravenhill.kuro.StdoutChannel
import cl.ravenhill.kuro.bufferedOutputChannel
import cl.ravenhill.kuro.logger
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class CoreTest : FreeSpec({
    beforeAny {
        Core.random = Random.Default
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
        Core.Dice.random = Random.Default
        Core.EvolutionLogger.level = Core.EvolutionLogger.DEFAULT_LEVEL
    }

    "The random number generator" - {
        "has a default value of Random.Default" {
            Core.random shouldBe Random.Default
        }

        "can be set to a new Random" {
            Core.random shouldBe Random.Default
            checkAll<Long> { seed ->
                val r = Random(seed)
                Core.random = r
                Core.random shouldBe r
            }
        }

        "can generate random numbers" {
            Core.random shouldBe Random.Default
            checkAll<Long> { seed ->
                val r = Random(seed)
                Core.random = Random(seed)
                repeat(100) {
                    r.nextDouble() shouldBe Core.random.nextDouble()
                }
            }
        }
    }

    "The maximum program depth" - {
        "has a default value of 7" {
            Core.DEFAULT_MAX_PROGRAM_DEPTH shouldBe 7
            Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
        }

        "can be set to a positive integer" {
            Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
            checkAll(Arb.positiveInt()) { depth ->
                Core.maxProgramDepth = depth
                Core.maxProgramDepth shouldBe depth
            }
        }

        "cannot be set to a non-positive integer" {
            Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
            checkAll(Arb.nonPositiveInt()) { depth ->
                shouldThrow<cl.ravenhill.enforcer.EnforcementException> {
                    depth.also { Core.maxProgramDepth = it }
                }.infringements.forEach { it shouldBeOfClass cl.ravenhill.enforcer.IntRequirementException::class }
            }
        }
    }

    "The Dice" - {
        "random number generator" - {
            "has a default value of Random.Default" {
                Core.Dice.random shouldBe Random.Default
            }

            "can be set to a new Random" {
                Core.Dice.random shouldBe Random.Default
                checkAll<Long> { seed ->
                    val r = Random(seed)
                    Core.Dice.random = r
                    Core.Dice.random shouldBe r
                }
            }

            "can generate random numbers" {
                Core.Dice.random shouldBe Random.Default
                checkAll<Long> { seed ->
                    val r = Random(seed)
                    Core.Dice.random = Random(seed)
                    repeat(100) {
                        r.nextDouble() shouldBe Core.Dice.random.nextDouble()
                    }
                }
            }

            "can generate a random integer between 0 and the given maximum" {
                checkAll(Arb.positiveInt(), Arb.long()) { max, seed ->
                    val (r1, r2) = Random(seed) to Random(seed)
                    Core.Dice.random = r1
                    Core.Dice.int(max) shouldBe r2.nextInt(max)
                }
            }

            "can generate a random probability" {
                checkAll(Arb.long()) { seed ->
                    val (r1, r2) = Random(seed) to Random(seed)
                    Core.Dice.random = r1
                    Core.Dice.probability() shouldBe r2.nextDouble()
                }
            }
        }
    }

    "Evolution logger" - {
        "default logging level should be Warn" {
            Core.EvolutionLogger.level shouldBe Core.EvolutionLogger.DEFAULT_LEVEL
        }

        "should be able to set the logging level" {
            checkAll(Arb.level()) { level ->
                Core.EvolutionLogger.level = level
                Core.EvolutionLogger.level shouldBeOfClass level::class
            }
        }

        "default logger should" - {
            "be named 'Evolution'" {
                Core.EvolutionLogger.logger.name shouldBe "Evolution"
            }

            "have one output standard output channel" {
                Core.EvolutionLogger.logger.compositeChannel.outputChannels.size shouldBe 1
                Core.EvolutionLogger.logger.compositeChannel.first()
                    .shouldBeOfClass(StdoutChannel::class)
            }

            "have a default level of Warn" {
                Core.EvolutionLogger.logger.level.shouldBeOfClass(Level.Warn::class)
            }
        }

        "be able to set a custom logger" {
            checkAll(Arb.loggers(Arb.list(Arb.string()))) { loggers ->
                loggers.forEach {
                    Core.EvolutionLogger.logger = it
                    Core.EvolutionLogger.logger.name shouldBe it.name
                }
            }
        }

        "be able to log a message at" - {
            "Trace level" {
                `check that the logger can log a message`(
                    Level.Trace(),
                    Core.EvolutionLogger::trace
                )
            }

            "Debug level" {
                `check that the logger can log a message`(
                    Level.Debug(),
                    Core.EvolutionLogger::debug
                )
            }

            "Info level" {
                `check that the logger can log a message`(
                    Level.Info(),
                    Core.EvolutionLogger::info
                )
            }

            "Warn level" {
                `check that the logger can log a message`(
                    Level.Warn(),
                    Core.EvolutionLogger::warn
                )
            }

            "Error level" {
                `check that the logger can log a message`(
                    Level.Error(),
                    Core.EvolutionLogger::error
                )
            }

            "Fatal level" {
                `check that the logger can log a message`(
                    Level.Fatal(),
                    Core.EvolutionLogger::fatal
                )
            }
        }
    }
})


/**
 * Returns an arbitrary `Level` from a list of predefined log levels:
 * - [Level.Trace]
 * - [Level.Debug]
 * - [Level.Info]
 * - [Level.Warn]
 * - [Level.Error]
 * - [Level.Fatal]
 */
private fun Arb.Companion.level() = arbitrary {
    element(
        Level.Trace(),
        Level.Debug(),
        Level.Info(),
        Level.Warn(),
        Level.Error(),
        Level.Fatal()
    ).bind()
}

/**
 * Suspends the current coroutine and checks that the logger can log a message at the given logging
 * level.
 * This function generates random messages using [Arb.Companion.string] and random logging levels
 * using [Arb.Companion.level].
 * The generated message is logged using the given [method] and the output is checked against the
 * expected format.
 * If the logging level is the same as the given [logMethodLevel], the output should match the
 * expected format.
 * If the logging level is different from the given [logMethodLevel], the output should be empty.
 *
 * @param logMethodLevel The logging level to use for this test.
 * @param method The method to use for logging the message.
 *
 * @throws AssertionError If the output does not match the expected format or is not empty when it
 * should be.
 */
suspend fun `check that the logger can log a message`(
    logMethodLevel: Level,
    method: (message: () -> String) -> Unit,
) {
    checkAll(Arb.string(), Arb.level()) { message, level ->
        assume {
            message.shouldNotBeBlank()
            message.shouldNotBeEmpty()
        }
        Core.EvolutionLogger.logger = logger("TestLogger") {
            bufferedOutputChannel()
            this.level = level
        }
        method { message }
        val output = Core.EvolutionLogger.logger.compositeChannel.first().toString()
        if (level <= logMethodLevel) {
            output shouldMatch logPattern("$logMethodLevel")
        } else {
            output shouldBe ""
        }
        Logger.clearActiveLoggers()
    }
}
