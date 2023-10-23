/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.utils.DoubleRange

/**
 * A chromosome that contains a list of [DoubleGene]s.
 * The genes represent the encoded variables of a solution in a genetic algorithm.
 *
 * The [range] and [filter] of a chromosome determine the valid values for each gene.
 * The [range] is a [Pair] of [Double] that defines the minimum and maximum possible values for each
 * gene.
 * The [filter] is a function that tests whether a given value satisfies the problem constraints for
 * a gene.
 * A gene is considered valid if its value is within the range and satisfies the predicate.
 *
 * @param genes The list of genes that this chromosome will contain.
 * @property range A pair of [Double]s that represents the range of the genes (``a to b``).
 * @property filter The filter to apply to the genes.
 *
 * @constructor Creates a new [DoubleChromosome] with the given [genes], [range], and [filter].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
data class DoubleChromosome(
    override val genes: List<DoubleGene>,
) : AbstractChromosome<Double, DoubleGene>(genes) {

    @Deprecated("To be removed in 2.0.0-A.2; use primary constructor instead.", ReplaceWith("DoubleChromosome(genes)"))
    constructor(ranges: List<DoubleRange>, filter: (Double) -> Boolean = {true}) : this(
        ranges.map { DoubleGene(Core.random.nextDouble(it.start, it.endInclusive), it) }
    )

    /* Documentation inherited from [Chromosome] */
    override fun withGenes(genes: List<DoubleGene>) = DoubleChromosome(genes)

    /**
     * This class represents a factory for generating instances of [DoubleChromosome].
     *
     * @property ranges the range of values for the genes in the chromosome.
     * @property size the size of the chromosome.
     * @property filter a filter function to apply to each gene.
     */
    class Factory : Chromosome.AbstractFactory<Double, DoubleGene>() {

        var ranges = mutableListOf<DoubleRange>()
        var filter: (Double) -> Boolean = { true }

        /* Documentation inherited from [Chromosome.Factory] */
        override fun make() = DoubleChromosome(ranges)

        /* Documentation inherited from [Any] */
        override fun toString() = "DoubleChromosome.Factory(ranges=$ranges, filter=$filter)"
    }
}
