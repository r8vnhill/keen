/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */
package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.BoolGene.False.dna
import cl.ravenhill.keen.genetic.genes.BoolGene.True.dna

/**
 * Represents a `BoolGene` which encapsulates the essence of a boolean gene in gene-based
 * evolutionary algorithms.
 * It provides various utility functions to work with its boolean DNA, like conversion to integer
 * or its string representation.
 *
 * @property dna The underlying boolean value that represents the gene's DNA.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
sealed class BoolGene : Gene<Boolean, BoolGene> {

    /* Documentation inherited from [Gene] */
    override fun generator() = Core.random.nextBoolean()

    /**
     * Creates a new `BoolGene` instance based on the provided DNA.
     *
     * @param dna The boolean value that should represent the new gene's DNA.
     * @return A new `BoolGene` instance (`True` or `False`) based on the given DNA.
     */
    override fun withDna(dna: Boolean) = if (dna) True else False

    /**
     * Converts the gene's DNA to its boolean representation.
     */
    fun toBool() = dna

    /**
     * Converts the gene's DNA to its integer representation, where `true` maps to `1` and
     * `false` maps to `0`.
     */
    fun toInt() = if (dna) 1 else 0

    /* Documentation inherited from [Any] */
    override fun toString() = "$dna"

    /**
     * Represents the `True` state of `BoolGene` where its DNA is set to `true`.
     *
     * @property dna Overrides the inherited DNA property with a `true` value.
     */
    object True : BoolGene() {
        override val dna = true
    }

    /**
     * Represents the `False` state of `BoolGene` where its DNA is set to `false`.
     *
     * @property dna Overrides the inherited DNA property with a `false` value.
     */
    object False : BoolGene() {
        override val dna = false
    }
}
