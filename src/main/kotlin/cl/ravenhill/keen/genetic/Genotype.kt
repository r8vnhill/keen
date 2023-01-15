/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.CollectionClause.NotBeEmpty
import cl.ravenhill.keen.Core.contracts
import cl.ravenhill.keen.IntClause.BeInRange
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import java.util.*


/**
 * A Genotype is a collection of chromosomes.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property chromosomes        The chromosomes of the Genotype
 * @property size               The size of the Genotype (number of chromosomes)
 */
class Genotype<DNA> private constructor(val chromosomes: List<Chromosome<DNA>>) :
    GeneticMaterial<DNA> {

    override fun verify() = chromosomes.isNotEmpty() && chromosomes.all { it.verify() }

    val size: Int = chromosomes.size

    override fun toString() = " [ ${chromosomes.joinToString(" | ")} ] "


    override fun flatten() =
        chromosomes.fold(mutableListOf<DNA>()) { acc, chromosome ->
            acc.apply { addAll(chromosome.flatten()) }
        }

    /**
     * Returns a new genotype with the given ``chromosomes``.
     */
    fun duplicate(chromosomes: List<Chromosome<DNA>>) = Genotype(chromosomes)

    fun sequence() = chromosomes.asSequence()

    operator fun get(index: Int): Chromosome<DNA> {
        contracts {
            index should BeInRange(0..size)
        }
        return chromosomes[index]
    }

    // region : equals and hashCode
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Genotype<*> -> false
        chromosomes != other.chromosomes -> false
        else -> true
    }

    override fun hashCode() = Objects.hash(Genotype::class, chromosomes)
    // endregion

    /**
     * Factory for [Genotype]s.
     *
     * @param DNA  The type of the DNA of the Genotype
     *
     * @property chromosomes Factories for the chromosomes of the Genotype
     */
    class Factory<DNA> {

        lateinit var chromosomes: MutableList<Chromosome.Factory<DNA>>

        /**
         * Adds a chromosome to the Genotype.
         *
         * @param lazyFactory  A factory for the chromosome
         */
        fun chromosome(lazyFactory: () -> Chromosome.Factory<DNA>) {
            if (!this::chromosomes.isInitialized) chromosomes = mutableListOf()
            chromosomes.add(lazyFactory())
        }

        /**
         * Creates a new [Genotype] with the given ``chromosomes``.
         */
        fun make(): Genotype<DNA> {
            contracts {
                if (this@Factory::chromosomes.isInitialized) {
                    chromosomes should NotBeEmpty
                }
                clause("Chromosomes should be initialized") {
                    this@Factory::chromosomes.isInitialized
                }
            }
            return Genotype(chromosomes.map { it.make() })
        }

        override fun toString() = "GenotypeBuilder { " +
                "chromosomes: $chromosomes }"
    }
}
