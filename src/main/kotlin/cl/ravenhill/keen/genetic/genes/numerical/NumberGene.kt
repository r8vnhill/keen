/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Represents a gene that stores a number value of type [DNA].
 *
 * @param DNA The type of the number value stored by this gene.
 * @property filter A predicate function that determines whether a number should be accepted as
 * valid for this gene.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface NumberGene<DNA : Number, G: NumberGene<DNA, G>> : Gene<DNA, G> {
    val filter: (DNA) -> Boolean

    /**
     * Computes the average value of this gene and the given list of genes.
     *
     * @param genes A list of [NumberGene] objects to compute the average with.
     * @return A new [NumberGene] object that represents the average value.
     */
    fun average(genes: List<G>): G

    /**
     * Converts this gene to a [Double].
     */
    fun toDouble(): Double

    /**
     * Converts this gene to an [Int].
     */
    fun toInt(): Int

    /// Documentation inherited from [Gene]
    override fun mutate() =
        withDna(
            generateSequence { generator() }
                .filter(filter)
                .first())
}