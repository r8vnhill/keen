/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.IntConstraint
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.util.MutableFilterCollection
import cl.ravenhill.keen.util.MutableRangedCollection

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

    /* Documentation inherited from [Chromosome] */
    override fun withGenes(genes: List<DoubleGene>) = DoubleChromosome(genes)

    /**
     * This class represents a factory for generating instances of [DoubleChromosome].
     *
     * @property size the size of the chromosome.
     */
    class Factory :
        Chromosome.AbstractFactory<Double, DoubleGene>(),
        MutableRangedCollection<Double>,
        MutableFilterCollection<Double> {

        override var ranges = mutableListOf<ClosedRange<Double>>()
        override var filters = mutableListOf<(Double) -> Boolean>()

        /* Documentation inherited from [Chromosome.Factory] */
        override fun make(): DoubleChromosome {
            enforceConstraints()
            when (ranges.size) {
                0 -> ranges = MutableList(size) { -Double.MAX_VALUE..Double.MAX_VALUE }
                1 -> ranges = MutableList(size) { ranges.first() }
            }
            when (filters.size) {
                0 -> filters = MutableList(size) { { _: Double -> true } }
                1 -> filters = MutableList(size) { filters.first() }
            }
            return DoubleChromosome(
                List(size) {
                    DoubleGene(
                        Core.random.nextDouble(ranges[it].start, ranges[it].endInclusive),
                        ranges[it],
                        filters[it]
                    )
                }
            )
        }

        private fun enforceConstraints() {
            constraints {
                if (ranges.size > 1) {
                    (
                        "When creating a chromosome with more than one range, the number of ranges " +
                            "must be equal to the number of genes"
                        ) {
                        ranges.size must IntConstraint.BeEqualTo(size)
                    }
                }
                if (filters.size > 1) {
                    (
                        "When creating a chromosome with more than one filter, the number of " +
                            "filters must be equal to the number of genes"
                        ) {
                        filters.size must IntConstraint.BeEqualTo(size)
                    }
                }
            }
        }
    }
}
