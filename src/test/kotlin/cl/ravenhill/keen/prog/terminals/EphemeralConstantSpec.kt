package cl.ravenhill.keen.prog.terminals

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import java.util.Random
import io.kotest.property.checkAll


class EphemeralConstantSpec : WordSpec({
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