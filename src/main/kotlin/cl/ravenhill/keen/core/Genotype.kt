/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.core

import cl.ravenhill.keen.core.chromosomes.Chromosome
import cl.ravenhill.keen.signals.GenotypeConfigurationException


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
    fun copy(chromosomes: List<Chromosome<DNA>>) = Genotype(chromosomes, fitnessFunction)

    class Builder<DNA> {

        lateinit var fitnessFunction: (Genotype<DNA>) -> Double
        lateinit var chromosomes: List<Chromosome.Builder<DNA>>

        fun build() = if (!this::chromosomes.isInitialized) {
            throw GenotypeConfigurationException { "Chromosomes must be initialized." }
        } else if (chromosomes.isEmpty()) {
            throw GenotypeConfigurationException { "Chromosomes must not be empty." }
        } else {
            Genotype(chromosomes.map { it.build() }, fitnessFunction)
        }

        override fun toString() = "GenotypeBuilder { chromosomes: $chromosomes }"
    }
}
