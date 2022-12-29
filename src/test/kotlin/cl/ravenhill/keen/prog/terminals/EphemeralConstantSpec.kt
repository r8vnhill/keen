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
            `check that a terminal should always have arity 0`(Arb.ephemeralConstant())
        }
    }
    "Descendants" When {
        "created without children" should {
            "return an empty list" {
                `check that a terminal should always have an empty list of descendants`(
                    Arb.ephemeralConstant()
                )
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
    "Flattening" should {
        "return a list with the terminal" {
            `check that a terminal should always flatten to a list with itself`(
                Arb.ephemeralConstant()
            )
        }
    }
    "Object identity" When {
        "equality" should {
            "be true for the same ephemeral constant" {
                `check that an object should always be equal to itself`(
                    Arb.ephemeralConstant()
                )
            }
            "be true for two ephemeral constants with the same value" {
                `check that an object should always be equal to a copy of itself`(
                    Arb.ephemeralConstant()
                )
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
    "Size" should {
        "be 1" {
            checkAll(Arb.ephemeralConstant()) { ephemeralConstant ->
                ephemeralConstant.size shouldBe 1
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