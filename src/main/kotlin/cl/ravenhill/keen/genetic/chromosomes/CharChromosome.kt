package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.CharGene
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
 * @property ranges The ranges of possible character values for each gene.
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

    val ranges: List<CharRange> by lazy { genes.map { it.range } }
    val filters: List<(Char) -> Boolean> by lazy { genes.map { it.filter } }

    constructor(genes: List<CharGene>, ranges: List<CharRange>, filter: (Char) -> Boolean) : this(
        genes
    ) {
        enforce {
            "The number of ranges must be equal to the number of genes" {
                ranges.size must BeEqualTo(genes.size)
            }
        }
    }

    constructor (
        genes: List<CharGene>,
        range: CharRange,
        filter: (Char) -> Boolean =
            { true },
    ) : this(
        genes,
        List(genes.size) { range },
        filter
    )

    /**
     * Creates a new `CharChromosome` instance with a random set of [CharGene] instances.
     *
     * @param size The size of the new chromosome.
     * @param filter The function used to filter the possible character values for each gene.
     *  Defaults to a function that accepts all character values.
     * @param range The range of possible character values for each gene.
     *  Defaults to ``' '..'z'``.
     */
    constructor(
        size: Int,
        range: CharRange = ' '..'z',
        filter: (Char) -> Boolean = { true },
    ) : this(
        List(size) { CharGene(Core.random.nextChar(range, filter), range, filter) },
        range = range
    )

    // Documentation inherited from Chromosome
    override fun withGenes(genes: List<CharGene>) = CharChromosome(genes)

    /**
     * Converts the chromosome's gene sequence into a simple string representation.
     * Each gene's DNA value is concatenated without any spaces or separators.
     *
     * @return A string representation of the chromosome's gene sequence.
     * @see Gene
     */
    fun toSimpleString() = genes.joinToString("") { it.dna.toString() }

    /**
     * A factory for creating [CharChromosome] instances with a random set of genes.
     *
     * @property size The size of the new chromosome.
     *  Defaults to `1`.
     * @property range The range of possible character values for each gene.
     * Defaults to ``' '..'z'``.
     * @property filter The function used to filter the possible character values for each gene.
     * Defaults to a function that accepts all character values.
     */
    class Factory : Chromosome.AbstractFactory<Char, CharGene>() {
        var ranges: List<CharRange> = listOf(' '..'z')

        @Deprecated("Use filters instead", ReplaceWith("filters"))
        var filter: (Char) -> Boolean = { true }
        var filters: List<(Char) -> Boolean> = listOf { true }

        /**
         * Creates a new [CharChromosome] instance with the current factory settings.
         */
        override fun make(): CharChromosome {
            enforce {
                if (ranges.size != 1) {
                    "The number of ranges must be either 1 or equal to the number of genes" {
                        ranges.size must BeEqualTo(size)
                    }
                    "The number of filters must be either 1 or equal to the number of genes" {
                        filters.size must BeEqualTo(size)
                    }
                }
            }
            if (ranges.size == 1) {
                ranges = List(size) { ranges.first() }
            }
            if (filters.size == 1) {
                filters = List(size) { filters.first() }
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

        // Documentation inherited from Any
        override fun toString() = "CharChromosome.Factory(size=$size, range=$ranges, filter=$filters)"
    }
}
