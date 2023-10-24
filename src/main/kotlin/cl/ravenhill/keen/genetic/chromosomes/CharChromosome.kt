package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.util.MutableRangedCollection
import cl.ravenhill.keen.util.nextChar

/**
 * A chromosome that consists of genes representing character values.
 *
 * The `CharChromosome` class implements the [Chromosome] interface and represents a collection
 * of [CharGene] instances.
 * A `CharChromosome` instance contains a list of character genes within a specified [range] and
 * [filter] function.
 * The [withGenes] function returns a new `CharChromosome` instance with the specified list of genes
 * and the same [range] and [filter] as the original.
 *
 * @property genes The list of character genes that make up the chromosome.
 * @property filter The function used to filter the possible character values for each gene.
 *  Defaults to a function that accepts all character values.
 *
 * @constructor Creates a new `CharChromosome` instance with the specified list of [genes], [range],
 *  and [filter].
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
data class CharChromosome(override val genes: List<CharGene>) :
    AbstractChromosome<Char, CharGene>(genes) {

    // Documentation inherited from Chromosome
    override fun withGenes(genes: List<CharGene>) = CharChromosome(genes)

    /**
     * Converts the chromosome's gene sequence into a simple string representation.
     * Each gene's DNA value is concatenated without any spaces or separators.
     *
     * @return A string representation of the chromosome's gene sequence.
     * @see CharGene
     */
    fun toSimpleString() = genes.joinToString("") { it.dna.toString() }

    /**
     * A factory for creating [CharChromosome] instances with a random set of genes.
     *
     * @property size The size of the new chromosome.
     *  Defaults to `1`.
     * @property range The range of possible character values for each gene.
     * Defaults to ``' '..'z'``.
     */
    class Factory : Chromosome.AbstractFactory<Char, CharGene>(), MutableRangedCollection<Char> {
        override var ranges = mutableListOf<ClosedRange<Char>>()
        var filters: MutableList<(Char) -> Boolean> = mutableListOf()

        /**
         * Creates a new [CharChromosome] instance with the current factory settings.
         */
        override fun make(): CharChromosome {
            enforceConstraints()
            when (ranges.size) {
                0 -> ranges = MutableList(size) { ' '..'z' }
                1 -> ranges = MutableList(size) { ranges.first() }
            }
            when (filters.size) {
                0 -> filters = MutableList(size) { { _: Char -> true } }
                1 -> filters = MutableList(size) { filters.first() }
            }
            return CharChromosome(
                List(size) {
                    CharGene(
                        Core.random.nextChar(ranges[it], filters[it]),
                        ranges[it],
                        filters[it]
                    )
                }
            )
        }

        private fun enforceConstraints() {
            enforce {
                if (ranges.size > 1) {
                    (
                        "When creating a chromosome with more than one range, the number of ranges " +
                            "must be equal to the number of genes"
                        ) {
                        ranges.size must BeEqualTo(size)
                    }
                }
                if (filters.size > 1) {
                    (
                        "When creating a chromosome with more than one filter, the number of " +
                            "filters must be equal to the number of genes"
                        ) {
                        filters.size must BeEqualTo(size)
                    }
                }
            }
        }

        // Documentation inherited from Any
        override fun toString() =
            "CharChromosome.Factory(size=$size, range=$ranges, filter=$filters)"
    }
}
