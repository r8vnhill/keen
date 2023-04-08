/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.CoroutineConstructor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.requirements.PairRequirement.StrictlyOrdered
import cl.ravenhill.keen.util.Filterable
import java.util.Objects
import kotlin.properties.Delegates

typealias IntToInt = Pair<Int, Int>

/**
 * A chromosome that contains a list of [IntGene]s.
 *
 * @param genes The list of genes that this chromosome will contain.
 * @property range A pair of [Int]s that represents the range of the genes (``a to b``).
 * @property predicate The filter to apply to the genes.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class IntChromosome private constructor(
    genes: List<IntGene>,
    val range: IntToInt,
    override val predicate: (Int) -> Boolean
) : AbstractChromosome<Int>(genes), Filterable<Int> {

    /**
     * Creates a new [IntChromosome] from a given [size], [range] and a [predicate]
     *
     * @param size The size of the chromosome.
     * @param range The range of the genes.
     * @param predicate The filter to apply to the genes.
     */
    private constructor(
        size: Int,
        range: IntToInt,
        predicate: (Int) -> Boolean,
        constructorExecutor: ConstructorExecutor<IntGene>
    ) : this(constructorExecutor(size) {
        IntGene(
            generateSequence { Core.random.nextInt(range.first, range.second) }
                .filter(predicate)
                .first(),
            range.first to range.second, predicate
        )
    }, range, predicate)

    @Suppress("UNCHECKED_CAST")
    override fun withGenes(genes: List<Gene<Int>>) =
        IntChromosome(genes as List<IntGene>, range, predicate)

    // region : equals, hashCode and toString
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is IntChromosome -> false
        else -> genes == other.genes
    }

    override fun hashCode() = Objects.hash(IntChromosome::class, genes)

    override fun toString() = "${genes.map { it.dna }}"
    // endregion
    /**
     * A [Chromosome.Factory] for [IntChromosome]s.
     *
     * @property size The size of the chromosome.
     * @property range The range of the genes.
     * @property filter The filter to apply to the genes.
     *
     * @constructor Creates a new [IntChromosome.Factory].
     */
    class Factory : Chromosome.Factory<Int> {

        var filter: (Int) -> Boolean = { true }
        lateinit var range: Pair<Int, Int>
        var size by Delegates.notNull<Int>()
        var executor: ConstructorExecutor<IntGene> = SequentialConstructor()

        override fun make(): IntChromosome {
            enforce {
                size should BePositive()
                range should StrictlyOrdered()
            }
            return IntChromosome(size, range, filter, executor)
        }

        override fun toString() = "IntChromosome.Builder { " +
                "size: $size, " +
                "range: $range," +
                "filter: $filter }"
    }
}
