package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.char.shouldBeInRange
import io.kotest.property.checkAll
import kotlin.random.Random


class RandomsSpec : WordSpec({
    "Generating a random character" should {
        "return a character in the range of printable characters" {
            checkAll<Long> { seed ->
                Core.rng = Random(seed)
                Core.rng.nextChar() shouldBeInRange
                        0.toChar()..Character.MAX_CODE_POINT.toChar()
            }
        }
    }
})