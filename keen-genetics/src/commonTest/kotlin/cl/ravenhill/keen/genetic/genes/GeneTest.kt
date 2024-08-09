/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetics.genes.Gene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

class GeneTest : FreeSpec({
    "A Gene" - {
        "can be mutated" {
            checkAll(arbSimpleGene()) { gene ->
                val mutated = gene.mutate()
                mutated.value shouldBe gene.value + 1
            }
        }

        "can be flattened" {
            checkAll(arbSimpleGene()) { gene ->
                gene.flatten() shouldBe listOf(gene.value)
            }
        }
    }
})

fun arbSimpleGene(isValid: Arb<Boolean> = Arb.constant(true)): Arb<SimpleGene> =
    Arb.int(Int.MIN_VALUE..<Int.MAX_VALUE).flatMap { size ->
        isValid.map { valid -> SimpleGene(size, valid) }
    }

data class SimpleGene(override val value: Int, val isValid: Boolean = true) : Gene<Int, SimpleGene> {
    override val generator: (Int, Random) -> Int = { v, _ -> v + 1 }
    override fun duplicateWithValue(value: Int) = copy(value = value)
    override fun verify(): Boolean {
        return if (!isValid) {
            false
        } else {
            super.verify()
        }
    }
}
