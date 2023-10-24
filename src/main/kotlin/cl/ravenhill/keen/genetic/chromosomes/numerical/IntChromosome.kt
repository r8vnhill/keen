/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement
import cl.ravenhill.enforcer.requirements.PairRequirement.BeStrictlyOrdered
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.utils.IntToInt

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

    @Deprecated("Prefer using the primary constructor and/or the chromosome factory")
    constructor(genes: List<IntGene>, ranges: List<IntRange>, filter: (Int) -> Boolean) : this(
        genes
    )

    @Deprecated("Prefer using the primary constructor and/or the chromosome factory")
    constructor(genes: List<IntGene>, range: IntToInt, filter: (Int) -> Boolean) : this(
        genes,
        List(genes.size) { range.first..range.second },
        filter
    )

    /**
     * Creates a new [IntChromosome] from a given [size], [range] and a [predicate]
     *
     * @param size The size of the chromosome.
     * @param range The range of the genes.
     * @param predicate The filter to apply to the genes.
     * @param constructorExecutor The executor to use for creating the genes.
     */
    @Deprecated("Prefer using the primary constructor and/or the chromosome factory")
    constructor(
        size: Int,
        range: IntToInt,
        predicate: (Int) -> Boolean,
        constructorExecutor: ConstructorExecutor<IntGene>,
    ) : this(
        constructorExecutor(size) {
            enforce { "The range must be ordered" { range must BeStrictlyOrdered() } }
            IntGene(
                generateSequence { Core.random.nextInt(range.first, range.second) }
                    .filter(predicate)
                    .first(),
                range.first to range.second,
                predicate
            )
        },
        range,
        predicate
    )

    // / Documentation inherited from [Chromosome]
    override fun withGenes(genes: List<IntGene>) = IntChromosome(genes)

    /**
     * A [Chromosome.Factory] for [IntChromosome]s.
     *
     * @property range The range of the genes.
     * @property filter The filter to apply to the genes.
     *
     * @constructor Creates a new [IntChromosome.Factory].
     */
    class Factory : Chromosome.AbstractFactory<Int, IntGene>() {
        var ranges = mutableListOf<IntRange>()
        var filters = mutableListOf<(Int) -> Boolean>()

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
                        Core.random.nextInt(ranges[it].first, ranges[it].last),
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
                        ranges.size must IntRequirement.BeEqualTo(size)
                    }
                }
                if (filters.size > 1) {
                    (
                        "When creating a chromosome with more than one filter, the number of " +
                            "filters must be equal to the number of genes"
                        ) {
                        filters.size must IntRequirement.BeEqualTo(size)
                    }
                }
            }
        }
    }
}
