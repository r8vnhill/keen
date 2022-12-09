package cl.ravenhill.keen

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlin.random.Random


class CoreSpec : StringSpec({
    "Core should be able to generate random numbers" {
        checkAll<Long> { seed ->
            val r = Random(seed)
            Core.rng = Random(seed)
            repeat(100) {
                r.nextDouble() shouldBe Core.rng.nextDouble()
            }
        }
    }
})