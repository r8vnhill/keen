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
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
    }

    "Core should be able to generate random numbers" {
        checkAll<Long> { seed ->
            val r = Random(seed)
            Core.random = Random(seed)
            repeat(100) {
                r.nextDouble() shouldBe Core.random.nextDouble()
            }
        }
    }

    "The maximum program depth" - {
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
})

/**
 * Checks whether the class of this object is equal to the specified [kClass].
 * Throws an AssertionError if the classes are not equal.
 * @param kClass the expected class of this object.
 */
private infix fun Any.shouldBeOfClass(kClass: KClass<*>) = this::class shouldBe kClass
