/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genes

import kotlin.random.Random

/**
 * Represents a gene with a Boolean value in an evolutionary algorithm.
 *
 * The `BooleanGene` sealed interface models a gene that can take on a Boolean value, with two possible implementations:
 * [True] and [False]. This interface provides basic operations for Boolean genes, including duplication and conversion
 * to an integer.
 *
 * ## Usage:
 * Use this interface when you need a gene that represents a binary state (true/false) in an evolutionary algorithm.
 * It supports random generation of Boolean values and allows for operations like converting the Boolean value to an
 * integer.
 *
 * ### Example:
 * Using `BooleanGene` in a genetic algorithm:
 * ```kotlin
 * val gene: BooleanGene = BooleanGene.True
 * println(gene.value) // Output: true
 * println(gene.toInt()) // Output: 1
 *
 * val randomGene = gene.generator(Random())
 * println(randomGene) // Output: true or false (randomly generated)
 *
 * val duplicatedGene = gene.duplicateWithValue(false)
 * println(duplicatedGene.value) // Output: false
 * ```
 *
 * @property generator A lambda that generates a random Boolean value using a given [Random] instance.
 * @property value The Boolean value held by the gene.
 */
sealed interface BooleanGene : Gene<Boolean, BooleanGene> {

    /**
     * A generator function that produces a random Boolean value using a given [Random] instance.
     */
    override val generator: (Random) -> Boolean
        get() = { it.nextBoolean() }

    /**
     * Duplicates the gene with the specified Boolean value.
     *
     * @param value The Boolean value to assign to the duplicated gene.
     * @return A new instance of `BooleanGene` with the specified value (`True` or `False`).
     */
    override fun duplicateWithValue(value: Boolean): BooleanGene = if (value) True else False

    /**
     * Converts the Boolean value of the gene to an integer.
     *
     * @return 1 if the value is `true`, 0 if the value is `false`.
     */
    fun toInt(): Int = if (value) 1 else 0

    /**
     * Represents a gene with a value of `true`.
     */
    data object True : BooleanGene {
        override val value: Boolean = true
    }

    /**
     * Represents a gene with a value of `false`.
     */
    data object False : BooleanGene {
        override val value: Boolean = false
    }
}
