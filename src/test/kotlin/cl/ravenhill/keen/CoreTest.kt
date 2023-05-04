package cl.ravenhill.keen

import cl.ravenhill.keen.requirements.Requirement
import cl.ravenhill.keen.util.logging.Level
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.pair
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


class CoreTest : FreeSpec({
    beforeAny {
        Core.random = Random.Default
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
        Core.skipChecks = false
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
                shouldThrow<EnforcementException> {
                    depth.also { Core.maxProgramDepth = it }
                }.violations.forEach { it shouldBeOfClass IntRequirementException::class }
            }
        }
    }

    "The skip checks flag" - {
        "has a default value of false" {
            Core.skipChecks shouldBe false
        }

        "can be set to true" {
            Core.skipChecks shouldBe false
            Core.skipChecks = true
            Core.skipChecks shouldBe true
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
        }
    }

    "Enforcement" - {
        "Scope" - {
            "StringScope" - {
                "should add a success to the results when a `should` requirement is met" {
                    checkAll(Arb.stringScope(), Arb.list(Arb.requirement())) { scope, reqs ->
                        with(scope) {
                            reqs.forEach { Any() should it }
                        }
                        scope.outerScope.results.size shouldBe reqs.size
                        scope.outerScope.results.filter { it.isSuccess }.size shouldBe reqs.size
                    }
                }

                "should add a failure to the results when a `should` requirement is not met" {
                    checkAll(
                        Arb.stringScope(),
                        Arb.list(Arb.requirement(Arb.constant(false)))
                    ) { scope, reqs ->
                        with(scope) {
                            reqs.forEach { Any() should it }
                        }
                        scope.outerScope.results.size shouldBe reqs.size
                        scope.outerScope.errors.size shouldBe reqs.size
                    }
                }

                "should add a success to the results when a predicate requirement is met" {
                    checkAll(Arb.stringScope(), Arb.list(Arb.constant(true))) { scope, trues ->
                        trues.forEach { scope.requirement { it } }
                        scope.outerScope.results.size shouldBe trues.size
                        scope.outerScope.results.filter { it.isSuccess }.size shouldBe trues.size
                    }
                }

                "should add a failure to the results when a predicate requirement is not met" {
                    checkAll(Arb.stringScope(), Arb.list(Arb.constant(false))) { scope, falses ->
                        falses.forEach { scope.requirement { it } }
                        scope.outerScope.results.size shouldBe falses.size
                        scope.outerScope.errors.size shouldBe falses.size
                    }
                }
            }

            "should be able to create a string scope from a string" {
                checkAll(Arb.string()) { message ->
                    val scope = Core.EnforceScope()
                    val expectedScope = Core.EnforceScope().StringScope(message)
                    lateinit var strScope: Core.EnforceScope.StringScope
                    with(scope) {
                        strScope = message.invoke { true }
                    }
                    strScope shouldBe expectedScope
                    strScope should haveSameHashCodeAs(expectedScope)
                }
            }
        }

        "If `skipChecks` is true then no exceptions should be thrown" {
            Core.skipChecks = true
            checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement(Arb.boolean())))) { pairs ->
                shouldNotThrow<EnforcementException> {
                    Core.enforce {
                        pairs.forEach { (message, req) ->
                            message.invoke { Any() should req }
                        }
                    }
                }
            }
        }

        "If `skipChecks` is false then" - {
            "if all requirements are met then no exceptions should be thrown" {
                Core.skipChecks = false
                checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement()))) { messages ->
                    shouldNotThrow<EnforcementException> {
                        Core.enforce {
                            messages.forEach { (message, req) ->
                                message.invoke { Any() should req }
                            }
                        }
                    }
                }
            }

            "if any requirement is not met then an exception should be thrown" {
                Core.skipChecks = false
                checkAll(
                    Arb.list(
                        Arb.pair(
                            Arb.string(),
                            Arb.requirement(Arb.boolean())
                        )
                    )
                ) { pairs ->
                    assume(pairs.any { !it.second.validator(true) })
                    shouldThrow<EnforcementException> {
                        Core.enforce {
                            pairs.forEach { (message, req) ->
                                message.invoke { Any() should req }
                            }
                        }
                    }.violations.size shouldBe pairs.filter { !it.second.validator(true) }.size
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

        "default logger should be named 'Evolution'" {
            Core.EvolutionLogger.logger.name shouldBe "Evolution"
        }

        "default logger should have one output standard output channel" {
            Core.EvolutionLogger.logger.compositeChannel.outputChannels.size shouldBe 1
            Core.EvolutionLogger.logger.compositeChannel
        }
    }
})

/**
 * A helper function that creates an [Arb] instance that generates instances of [Requirement] for
 * testing.
 *
 * @param success Whether the generated [Requirement] instances should be successful.
 * Default is true.
 * @return An [Arb] instance that generates instances of [Requirement] for testing.
 */
private fun Arb.Companion.requirement(success: Arb<Boolean> = Arb.constant(true)) = arbitrary {
    val successBool = success.bind()
    object : Requirement<Any> {
        override val validator: (Any) -> Boolean
            get() = { successBool }

        override fun generateException(description: String) =
            object : UnfulfilledRequirementException({ description }) {}
    }
}

/**
 * Helper function that creates an instance of `Arb` for generating instances of
 * [Core.EnforceScope.StringScope] class.
 */
private fun Arb.Companion.stringScope() = arbitrary {
    val message = string().bind()
    Core.EnforceScope().StringScope(message)
}

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