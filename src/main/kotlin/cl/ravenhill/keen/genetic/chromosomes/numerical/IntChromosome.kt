/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.MutableFilterCollection
import cl.ravenhill.keen.util.MutableRangedCollection
import cl.ravenhill.keen.util.IntToInt

/**
 * A chromosome that contains a list of [IntGene]s.
 * The genes represent the encoded variables of a solution in a genetic algorithm.
 *
 * The [range] and [filter] of a chromosome determine the valid values for each gene.
 * The [range] is a [Pair] of [Int] that defines the minimum and maximum possible values for each
 * gene. The predicate is a function that tests whether a given value satisfies the problem
 * constraints for a gene.
 * A gene is considered valid if its value is within the range and satisfies the predicate.
 *
 * @param genes The list of genes that this chromosome will contain.
 * @property ranges The ranges of possible values for each gene.
 * @property filter The filter to apply to the genes.
 *
 * @constructor Creates a new [IntChromosome] with the given [genes], [range], and [filter].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
data class IntChromosome(override val genes: List<IntGene>) :
    AbstractChromosome<Int, IntGene>(genes) {

    /**
     * Secondary constructor that allows for the creation of an [IntChromosome] instance
     * using a variable number of [IntGene] arguments.
     *
     * @param genes Vararg of [IntGene] objects to be included in the chromosome.
     */
    constructor(vararg genes: IntGene) : this(genes.toList())

    override fun withGenes(genes: List<IntGene>) = IntChromosome(genes)

    override fun toSimpleString() =
        "[${genes.joinToString(", ") { it.toSimpleString() }}]"

    override fun toString(): String {
        return "IntChromosome(genes=[${genes.joinToString(", ") { it.toSimpleString() }}])"
    }

    /**
     * A [Chromosome.Factory] for [IntChromosome]s.
     *
     * @property range The range of the genes.
     * @property filter The filter to apply to the genes.
     *
     * @constructor Creates a new [IntChromosome.Factory].
     */
    class Factory :
        Chromosome.AbstractFactory<Int, IntGene>(),
        MutableRangedCollection<Int>,
        MutableFilterCollection<Int> {

        override var ranges = mutableListOf<ClosedRange<Int>>()
        override var filters = mutableListOf<(Int) -> Boolean>()

        @Deprecated("Use the list version instead", ReplaceWith("ranges += range"))
        var filter: (Int) -> Boolean = { true }

        @Deprecated("Use the list version instead", ReplaceWith("ranges += range"))
        lateinit var range: IntToInt

        // / Documentation inherited from [Chromosome.Factory]
        override fun make(): IntChromosome {
            enforceConstraints()
            when (ranges.size) {
                0 -> ranges = MutableList(size) { Int.MIN_VALUE..Int.MAX_VALUE }
                1 -> ranges = MutableList(size) { ranges.first() }
            }
            when (filters.size) {
                0 -> filters = MutableList(size) { { _: Int -> true } }
                1 -> filters = MutableList(size) { filters.first() }
            }
            return IntChromosome(
                List(size) {
                    IntGene(
                        Core.random.nextInt(ranges[it].start, ranges[it].endInclusive),
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
                        ranges must HaveSize(size)
                    }
                }
                if (filters.size > 1) {
                    (
                          "When creating a chromosome with more than one filter, the number of " +
                                "filters must be equal to the number of genes"
                          ) {
                        filters must HaveSize(size)
                    }
                }
            }
        }
    }
}
