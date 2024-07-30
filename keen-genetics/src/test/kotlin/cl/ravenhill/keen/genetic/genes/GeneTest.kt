/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetics.genes.Gene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
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

fun arbSimpleGene(): Arb<SimpleGene> = Arb.int(Int.MIN_VALUE..<Int.MAX_VALUE).map {
    SimpleGene(it)
}

data class SimpleGene(override val value: Int) : Gene<Int, SimpleGene> {
    override val generator: (Int, Random) -> Int = { v, _ -> v + 1 }
    override fun duplicateWithValue(value: Int) = copy(value = value)
}
