/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.GenotypeConfigurationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.util.validateNotEmpty
import cl.ravenhill.keen.util.validatePredicate


/**
 * A Genotype is a collection of chromosomes.
 *
 * @param DNA   The type of the DNA of the Genotype
 * @property chromosomes        The chromosomes of the Genotype
 * @property fitnessFunction    The fitness function of the Genotype
 * @property size               The size of the Genotype (number of chromosomes)
 * @property fitness            The fitness of the Genotype
 */
class Genotype<DNA> private constructor(
    val chromosomes: List<Chromosome<DNA>>,
    private val fitnessFunction: (Genotype<DNA>) -> Double
) : GeneticMaterial {
    override fun verify() = chromosomes.isNotEmpty() && chromosomes.all { it.verify() }

    val size: Int = chromosomes.size

    val fitness: Double
        get() = fitnessFunction(this)

    override fun toString() = " [ ${chromosomes.joinToString(" | ")} ] "
    fun map(transform: (List<Chromosome<DNA>>) -> List<Chromosome<DNA>>) =
        Genotype(transform(chromosomes), fitnessFunction)

    /**
     * Returns a new genotype with the given ``chromosomes``.
     */
    fun duplicate(chromosomes: List<Chromosome<DNA>>) = Genotype(chromosomes, fitnessFunction)

    fun sequence() = chromosomes.asSequence()

    /**
     * Factory for [Genotype]s.
     *
     * @param DNA  The type of the DNA of the Genotype
     *
     * @property fitnessFunction The fitness function of the Genotype
     * @property chromosomes Factories for the chromosomes of the Genotype
     */
    class Factory<DNA>(var fitnessFunction: (Genotype<DNA>) -> Double = { Double.NaN }) {

        lateinit var chromosomes: List<Chromosome.Factory<DNA>>

        /**
         * Creates a new [Genotype] with the given ``chromosomes``.
         */
        fun make(): Genotype<DNA> {
            validatePredicate({ this::chromosomes.isInitialized }) { "Chromosomes must be initialized" }
            chromosomes.validateNotEmpty { "Chromosomes must not be empty" }
            return Genotype(chromosomes.map { it.make() }, fitnessFunction)
        }

        override fun toString() = "GenotypeBuilder { " +
                "fitnessFunction: $fitnessFunction" +
                "chromosomes: $chromosomes }"
    }
}
