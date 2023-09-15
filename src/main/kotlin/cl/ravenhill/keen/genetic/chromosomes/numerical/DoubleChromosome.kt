/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.util.Filterable
import cl.ravenhill.utils.DoubleToDouble
import java.util.Objects

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
class DoubleChromosome(
    genes: List<DoubleGene>,
    val range: Pair<Double, Double>,
    override val filter: (Double) -> Boolean
) : AbstractChromosome<Double, DoubleGene>(genes), Filterable<Double> {

    /**
     * Creates a new [DoubleChromosome] from a given [size], [range] and a [filter]
     *
     * @param size The size of the chromosome.
     * @param range The range of the genes.
     * @param filter The filter to apply to the genes.
     * @param constructorExecutor The executor to use for creating the genes.
     */
    constructor(
        size: Int,
        range: DoubleToDouble,
        filter: (Double) -> Boolean,
        constructorExecutor: ConstructorExecutor<DoubleGene>
    ) : this(
        if (range.first.isNaN() || range.second.isNaN()) {
            constructorExecutor(size) {
                DoubleGene(Double.NaN, range)
            }
        } else {
            constructorExecutor(size) {
                DoubleGene(Core.random.nextDouble(range.first, range.second), range)
            }
        },
        range,
        filter
    )

    /* Documentation inherited from [Chromosome] */
    override fun withGenes(genes: List<DoubleGene>) = DoubleChromosome(genes, range, filter)

    /* Documentation inherited from [Any] */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is DoubleChromosome -> false
        else -> genes == other.genes
    }

    /* Documentation inherited from [Any] */
    override fun hashCode() = Objects.hash(DoubleChromosome::class, genes, range)

    /**
     * This class represents a factory for generating instances of [DoubleChromosome].
     *
     * @property range the range of values for the genes in the chromosome.
     * @property size the size of the chromosome.
     * @property filter a filter function to apply to each gene.
     */
    class Factory : Chromosome.AbstractFactory<Double, DoubleGene>() {

        lateinit var range: DoubleToDouble
        var filter: (Double) -> Boolean = { true }

        /* Documentation inherited from [Chromosome.Factory] */
        override fun make() = DoubleChromosome(size, range, filter, executor)

        /* Documentation inherited from [Any] */
        override fun toString() = "DoubleChromosome.Builder { " +
            "size: $size, " +
            "range: $range, " +
            "executor: $executor }"
    }
}
