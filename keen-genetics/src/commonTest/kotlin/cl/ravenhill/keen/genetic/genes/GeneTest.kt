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

/**
 * Generates an arbitrary instance of `SimpleGene` with a random integer value and a validity flag.
 *
 * @param isValid An arbitrary boolean value indicating the validity of the gene. Defaults to always `true`.
 * @return An arbitrary `SimpleGene` instance with a random integer value and the specified validity.
 */
fun arbSimpleGene(
    isValid: Arb<Boolean> = Arb.constant(true)
): Arb<SimpleGene> =
    Arb.int(Int.MIN_VALUE..<Int.MAX_VALUE).flatMap { size ->
        isValid.map { valid -> SimpleGene(size, valid) }
    }

/**
 * Represents a simple gene with an integer value and a validity flag.
 *
 * @param value The integer value of the gene.
 * @param isValid A boolean indicating whether the gene is considered valid. Defaults to `true`.
 */
data class SimpleGene(
    override val value: Int,
    val isValid: Boolean = true
) : Gene<Int, SimpleGene> {

    /**
     * A function to generate a new integer value based on the current gene's value.
     * This function increments the current gene's value by 1.
     */
    override val generator: (Random) -> Int = { value + 1 }

    /**
     * Creates a new instance of `SimpleGene` with the specified value, preserving other properties.
     *
     * @param value The new integer value for the gene.
     * @return A new instance of `SimpleGene` with the updated value.
     */
    override fun copyWithValue(value: Int) = copy(value = value)

    /**
     * Verifies if the gene is valid. The gene is considered invalid if `isValid` is `false`. Otherwise, it calls the
     * `super.verify()` method.
     *
     * @return `true` if the gene is valid, otherwise `false`.
     */
    override fun verify() = if (!isValid) {
        false
    } else {
        super.verify()
    }
}
