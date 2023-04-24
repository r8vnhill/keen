package cl.ravenhill.keen

import cl.ravenhill.keen.requirements.Requirement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlin.random.Random


class CoreSpec : FreeSpec({
    beforeAny {
        Core.random = Random.Default
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
        Core.skipChecks = false
        Core.Dice.random = Random.Default
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
                "Should add a success to the results when a requirement is met" {
                    checkAll(Arb.stringScope(), Arb.list(Arb.requirement())) { scope, reqs ->
                        with(scope) {
                            reqs.forEach { Any() should it }
                        }
                        scope.outerScope.results.size shouldBe reqs.size
                        scope.outerScope.results.filter { it.isSuccess }.size shouldBe reqs.size
                    }
                }

                "Should add a failure to the results when a requirement is not met" {
                    checkAll(Arb.stringScope(), Arb.list(Arb.requirement(false))) { scope, reqs ->
                        with(scope) {
                            reqs.forEach { Any() should it }
                        }
                        scope.outerScope.results.size shouldBe reqs.size
                        scope.outerScope.errors.size shouldBe reqs.size
                    }
                }
            }
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
private fun Arb.Companion.requirement(success: Boolean = true) = arbitrary {
    object : Requirement<Any> {
        override val validator: (Any) -> Boolean
            get() = { success }

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
