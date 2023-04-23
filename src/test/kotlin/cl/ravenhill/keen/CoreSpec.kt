package cl.ravenhill.keen

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.random.Random
import kotlin.reflect.KClass


class CoreSpec : FreeSpec({
    beforeAny {
        Core.random = Random.Default
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
        Core.skipChecks = false
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
})
