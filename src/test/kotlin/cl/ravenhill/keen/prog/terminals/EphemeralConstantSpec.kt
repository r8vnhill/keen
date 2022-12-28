package cl.ravenhill.keen.prog.terminals

import io.kotest.core.spec.style.WordSpec
import io.kotest.core.spec.style.wordSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlin.random.Random

private fun checkCopy() {
    val r1 = Random(1)
    val r2 = Random(1)
    val constant = EphemeralConstant { r1.nextDouble() }
    constant.copy() shouldBe EphemeralConstant { r2.nextDouble() }
}

class EphemeralConstantSpec : WordSpec({
    "Copying" When {
        "shallow copying" should {
            "create a copy with the same generator function" {
                checkCopy()
            }
        }
        "deep copying" should {
            "create a copy with the same generator function" {
                checkCopy()
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
    "Ephemeral constant arity" should {
        "be 0" {
            val constant = EphemeralConstant { 1 }
            constant.arity shouldBe 0
        }
    }
})