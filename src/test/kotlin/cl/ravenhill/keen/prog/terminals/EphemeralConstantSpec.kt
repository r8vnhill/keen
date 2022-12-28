package cl.ravenhill.keen.prog.terminals

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class EphemeralConstantSpec : WordSpec({
    "Arity" should {
        "be 0" {
            checkAll(Arb.ephemeralConstant()) { ephemeralConstant ->
                ephemeralConstant.arity shouldBe 0
            }
        }
    }
    "Copying" When {
        "shallow copying" should {
            "create a copy with the same generator function" {
                checkCopy(Arb.ephemeralConstant()) { ephemeralConstant ->
                    ephemeralConstant.copy()
                }
            }
        }
        "deep copying" should {
            "create a copy with the same generator function" {
                checkCopy(Arb.ephemeralConstant()) { ephemeralConstant ->
                    ephemeralConstant.deepCopy() as EphemeralConstant
                }
            }
        }
    }
    "Object identity" When {
        "equality" should {
            "be true for the same ephemeral constant" {
                checkAll(Arb.ephemeralConstant()) { ephemeralConstant ->
                    ephemeralConstant shouldBe ephemeralConstant
                }
            }
            "be true for two ephemeral constants with the same value" {
                checkAll(Arb.ephemeralConstant()) { ephemeralConstant ->
                    val copy = ephemeralConstant.deepCopy()
                    ephemeralConstant shouldBe copy
                }
            }
            "be false for two ephemeral constants with different values" {
                checkAll(
                    Arb.ephemeralConstant(),
                    Arb.ephemeralConstant()
                ) { ephemeralConstant1, ephemeralConstant2 ->
                    assume(ephemeralConstant1(arrayOf()) != ephemeralConstant2(arrayOf()))
                    ephemeralConstant1 shouldNotBe ephemeralConstant2
                }
            }
        }
    }
    "Reducing an ephemeral constant" should {
        "generate a constant value if the generator function returns a constant value" {
            checkAll<Int> { value ->
                val constant = EphemeralConstant { value }
                constant(arrayOf()) shouldBe value
            }
        }
        "generate a random value if the generator function returns a random value" {
            checkAll<Long> { seed ->
                val constant = EphemeralConstant { Random(seed).nextInt() }
                constant(arrayOf()) shouldBe Random(seed).nextInt()
            }
        }
    }
})

/**
 * Constructs an arbitrary ephemeral constant.
 */
fun Arb.Companion.ephemeralConstant() = arbitrary {
    val v = Arb.double().bind()
    EphemeralConstant { v }
}