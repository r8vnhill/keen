package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.Gene
import java.util.*


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
 * @property range The range of possible character values for each gene.
 * @property filter The function used to filter the possible character values for each gene.
 *  Defaults to a function that accepts all character values.
 *
 * @constructor Creates a new `CharChromosome` instance with the specified list of [genes], [range],
 *  and [filter].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class CharChromosome(
    override val genes: List<CharGene>,
    val range: CharRange,
    private val filter: (Char) -> Boolean = { true }
) : Chromosome<Char, CharGene> {


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
        filter: (Char) -> Boolean = { true },
        range: CharRange = ' '..'z'
    ) : this(List(size) { CharGene.create(range, filter) }, range = ' '..'z')

    // Documentation inherited from Chromosome
    override fun withGenes(genes: List<CharGene>) = CharChromosome(genes, range, filter)

    // Documentation inherited from Verifiable
    override fun verify() = genes.all { filter(it.dna) }

    // Documentation inherited from Any
    override fun toString(): String {
        return genes.joinToString("")
    }

    // Documentation inherited from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is CharChromosome -> false
        other::class != this::class -> false
        else -> genes == other.genes
    }

    // Documentation inherited from Any
    override fun hashCode() = Objects.hash(CharChromosome::class, genes)

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
        var size = 1
        var range = ' '..'z'
        var filter: (Char) -> Boolean = { true }

        /**
         * Creates a new [CharChromosome] instance with the current factory settings.
         */
        override fun make() = CharChromosome(size, filter, range)

        // Documentation inherited from Any
        override fun toString() = "CharChromosome.Builder { size: $size }"
    }
}
