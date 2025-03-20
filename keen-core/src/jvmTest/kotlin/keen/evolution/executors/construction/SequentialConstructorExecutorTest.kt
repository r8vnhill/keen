/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package keen.evolution.executors.construction

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class SequentialConstructorExecutorTest : FreeSpec({
    "SequentialConstructorExecutor" - {
        "constructs the right amount of elements" {
            checkAll(Arb.int(0..1000)) { size ->
                val sequence = SequentialConstructor(size) { it }
                sequence.size shouldBe size
            }
        }
    }
})
