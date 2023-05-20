/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.enforcer.requirements.PairRequirement.BeStrictlyOrdered
import cl.ravenhill.keen.util.Filterable
import cl.ravenhill.keen.util.IntToInt
import java.util.Objects


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
 * @property range A pair of [Int]s that represents the range of the genes (``a to b``).
 * @property filter The filter to apply to the genes.
 *
 * @constructor Creates a new [IntChromosome] with the given [genes], [range], and [filter].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class IntChromosome(
    genes: List<IntGene>,
    val range: IntToInt,
    override val filter: (Int) -> Boolean
) : AbstractChromosome<Int, IntGene>(genes), Filterable<Int> {

    /**
     * Creates a new [IntChromosome] from a given [size], [range] and a [predicate]
     *
     * @param size The size of the chromosome.
     * @param range The range of the genes.
     * @param predicate The filter to apply to the genes.
     * @param constructorExecutor The executor to use for creating the genes.
     */
    constructor(
        size: Int,
        range: IntToInt,
        predicate: (Int) -> Boolean,
        constructorExecutor: ConstructorExecutor<IntGene>
    ) : this(constructorExecutor(size) {
        enforce { "The range must be ordered" { range must BeStrictlyOrdered() } }
        IntGene(
            generateSequence { Core.random.nextInt(range.first, range.second) }
                .filter(predicate)
                .first(),
            range.first to range.second, predicate
        )
    }, range, predicate)

    /// Documentation inherited from [Chromosome]
    override fun withGenes(genes: List<IntGene>) = IntChromosome(genes, range, filter)

    // region : equals, hashCode and toString
    /// Documentation inherited from [Any]
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is IntChromosome -> false
        else -> genes == other.genes
    }

    /// Documentation inherited from [Any]
    override fun hashCode() = Objects.hash(IntChromosome::class, genes)

    /// Documentation inherited from [Any]
    override fun toString() = "${genes.map { it.dna }}"
    // endregion

    /**
     * A [Chromosome.Factory] for [IntChromosome]s.
     *
     * @property range The range of the genes.
     * @property filter The filter to apply to the genes.
     *
     * @constructor Creates a new [IntChromosome.Factory].
     */
    class Factory : Chromosome.AbstractFactory<Int, IntGene>() {

        var filter: (Int) -> Boolean = { true }

        lateinit var range: IntToInt

        /// Documentation inherited from [Chromosome.Factory]
        override fun make() = IntChromosome(size, range, filter, executor)

        override fun toString() = "IntChromosome.Builder { " +
                "size: $size, " +
                "range: $range," +
                "filter: $filter," +
                "executor: $executor }"
    }
}
